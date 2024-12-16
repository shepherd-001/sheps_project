package com.shepherd.sheps_project.services;

import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;
import com.shepherd.sheps_project.data.models.Gender;
import com.shepherd.sheps_project.data.models.Role;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registrationRequest) {
        checkIfUserExists(registrationRequest.getEmail());
        User newUser = User.builder()
                .firstName(registrationRequest.getFirstName().trim())
                .lastName(registrationRequest.getLastName().trim())
                .email(registrationRequest.getEmail().toLowerCase().trim())
                .password(registrationRequest.getPassword())
                .gender(Gender.valueOf(registrationRequest.getGender().toUpperCase().trim()))
                .roles(Set.of(Role.valueOf(registrationRequest.getRole().toUpperCase().trim())))
                .build();
        User savedUser = userRepository.save(newUser);
    //todo implement send email verification

        log.info("User with the first name {} registered successfully", savedUser.getFirstName());
        return getRegisterUserResponse(savedUser);
    }

    private void checkIfUserExists(String email) {
        if(userRepository.existsByEmail(email))
            throw new ResourceNotFoundException("User with the provided email already exists");
    }

    private static RegisterUserResponse getRegisterUserResponse(User savedUser) {
        return RegisterUserResponse.builder()
                .message("User registered successfully")
                .userId(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .roles(savedUser.getRoles())
                .isEnabled(savedUser.isEnabled())
                .build();
    }

//    @Override
//    public UserResponse getUserById(String userId) {
//        User user = findUserById(userId);
//        return getUserResponse(user);
//    }

//    private static UserResponse getUserResponse(User user) {
//        return UserResponse.builder()
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .gender(user.getGender())
//                .roles(user.getRoles())
//                .isEnabled(user.isEnabled())
//                .build();
//    }
//
//    private User findUserById(String userId) {
//        return userRepository.findById(userId).orElseThrow(()->
//                new NotFoundException("User with the provided id not found"));
//    }
//
//    @Override
//    public UserResponse getUserByEmail(String email) {
//        User user = findUserByEmail(email);
//        return getUserResponse(user);
//    }
//
//    private User findUserByEmail(String email) {
//        return userRepository.findByEmail(email).orElseThrow(()->
//                new NotFoundException("User with the provided email address not found"));
//    }

}
