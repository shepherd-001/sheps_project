package com.shepherd.sheps_project.services.userService;

import com.shepherd.sheps_project.data.dtos.responses.UserResponse;

public interface UserService {
    UserResponse getUserById(String userId);
    UserResponse getUserByEmail(String userEmail);
}
