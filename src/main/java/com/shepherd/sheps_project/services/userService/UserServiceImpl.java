package com.shepherd.sheps_project.services.userService;

import com.shepherd.sheps_project.data.dtos.responses.UserResponse;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserResponse getUserById(String userId) {
        User user =  userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with the provided id not found"));
        return buildUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                ()-> new ResourceNotFoundException("User with the provided email not found"));
        return buildUserResponse(user);
    }

    private UserResponse buildUserResponse(User user){
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .gender(user.getGender())
                .isEnabled(user.isEnabled())
                .build();
    }
}
