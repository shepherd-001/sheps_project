package com.shepherd.sheps_project.services;

import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;

public interface AuthService {
    RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest);

//    UserResponse getUserById(String userId);
//    UserResponse getUserByEmail(String email);
}
