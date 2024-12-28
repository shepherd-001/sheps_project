package com.shepherd.sheps_project.services.userService;

import com.shepherd.sheps_project.data.dtos.responses.PaginatedResponse;
import com.shepherd.sheps_project.data.dtos.responses.UserResponse;

public interface UserService {
    UserResponse getAuthenticatedUser();
    UserResponse getUserById(String id);
    PaginatedResponse<UserResponse> getAllUsers(int pageNumber);
    PaginatedResponse<UserResponse> getAllEnabledUsers(int pageNumber);
    PaginatedResponse<UserResponse> getAllDisabledUsers(int pageNumber);
}
