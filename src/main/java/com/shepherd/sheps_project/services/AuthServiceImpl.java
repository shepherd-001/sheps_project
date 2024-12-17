package com.shepherd.sheps_project.services;

import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;
import com.shepherd.sheps_project.data.models.Gender;
import com.shepherd.sheps_project.data.models.Role;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.*;
import com.shepherd.sheps_project.services.emailService.EmailValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        checkIfUserExists(email);
        validateEmail(email);
        User newUser = buildRegisterUserRequest(registrationRequest);
        User savedUser = userRepository.save(newUser);
        log.info("User with the first name {} registered successfully", savedUser.getFirstName());
//        todo implement send email verification
        return buildRegisterUserResponse(savedUser);
    }

    private void checkIfUserExists(String email) {
        if(userRepository.existsByEmail(email))
            throw new AlreadyExistsException("User with the provided email already exists");
    }

    private void validateEmail(String email) {
        Map<String, Object> validationResponse = emailValidationService.validateEmail(email);

        if ("valid".equals(validationResponse.get("status"))) {
            log.info("The email address is valid");
        } else if ("disposable".equals(validationResponse.get("sub_status")) || emailValidationService.isDisposableEmail(email)) {
            log.warn("The email address entered is disposable");
            throw new DisposableEmailException("Disposable email is not allowed");
        } else {
            log.error("Error validating email address");
            throw new EmailValidationException("Error validating email address. Try again with a valid email address");
        }
    }

    private static User buildRegisterUserRequest(RegisterUserRequest registrationRequest) {
        return User.builder()
                .firstName(registrationRequest.getFirstName().trim())
                .lastName(registrationRequest.getLastName().trim())
                .email(registrationRequest.getEmail().toLowerCase().trim())
                .password(registrationRequest.getPassword())
                .gender(Gender.valueOf(registrationRequest.getGender().toUpperCase().trim()))
                .roles(Set.of(Role.valueOf(registrationRequest.getRole().toUpperCase().trim())))
                .build();
    }

    private static RegisterUserResponse buildRegisterUserResponse(User savedUser) {
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

//
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
