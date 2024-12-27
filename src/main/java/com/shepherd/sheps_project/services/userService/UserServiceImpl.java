package com.shepherd.sheps_project.services.userService;

import com.shepherd.sheps_project.data.dtos.responses.UserResponse;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    @Override
    public UserResponse getAuthenticatedUser() {
        return mapToUserResponse(AppUtils.getCurrentUser());
    }

    private UserResponse mapToUserResponse(User user){
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
