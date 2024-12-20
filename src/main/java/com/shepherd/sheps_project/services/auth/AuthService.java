package com.shepherd.sheps_project.services.auth;

import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.EmailConfirmationResponse;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;

public interface AuthService {
    RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest);
    EmailConfirmationResponse verifyEmail(String token, String email);
}
