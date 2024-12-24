package com.shepherd.sheps_project.services.auth;

import com.shepherd.sheps_project.data.dtos.requests.ChangePasswordRequest;
import com.shepherd.sheps_project.data.dtos.requests.LoginRequest;
import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.requests.ResetPasswordRequest;
import com.shepherd.sheps_project.data.dtos.responses.*;


public interface AuthService {
    RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest);
    EmailConfirmationResponse verifyEmail(String token, String email);
    LoginResponse login(LoginRequest loginRequest);
    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest);
    String requestPasswordReset(String email);
    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}
