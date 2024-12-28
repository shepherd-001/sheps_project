package com.shepherd.sheps_project.controllers;

import com.shepherd.sheps_project.controllers.responses.BaseResponse;
import com.shepherd.sheps_project.data.dtos.requests.*;
import com.shepherd.sheps_project.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody RegisterUserRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.build(authService.registerUser(registerRequest)));
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verifyEmail(@RequestBody EmailConfirmationRequest emailConfirmationRequest) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(authService.verifyEmail(emailConfirmationRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(authService.login(loginRequest)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(authService.changePassword(changePasswordRequest)));
    }

    @PostMapping("/request-password-reset")
        public ResponseEntity<Object> requestPasswordReset(@Valid @RequestParam String email) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(authService.requestPasswordReset(email)));
    }

    @PostMapping("/reset-password")
        public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(authService.resetPassword(resetPasswordRequest)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(){
        return ResponseEntity.ok().body(BaseResponse.build("User logged out successfully"));
    }
}
