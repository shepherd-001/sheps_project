package com.shepherd.sheps_project.services.auth;

import com.shepherd.sheps_project.data.dtos.requests.*;
import com.shepherd.sheps_project.data.dtos.responses.*;


public interface AuthService {
    RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest);
    EmailConfirmationResponse verifyEmail(EmailConfirmationRequest emailConfirmationRequest);
    LoginResponse login(LoginRequest loginRequest);
    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest);
    ResetPasswordResponse requestPasswordReset(String email);
    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}
