package com.shepherd.sheps_project.controllers;

import com.shepherd.sheps_project.controllers.responses.BaseResponse;
import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.services.AuthService;
import com.shepherd.sheps_project.services.emailService.EmailValidationService;
import com.shepherd.sheps_project.services.passwordServie.PasswordValidationService;
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
    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;


    @PostMapping("signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody RegisterUserRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.build(authService.registerUser(registerRequest)));
    }

    @GetMapping("validate-email")
    public ResponseEntity<Object> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(emailValidationService.isValidEmail(email)));
    }

    @GetMapping("validate-password")
    public ResponseEntity<Object> validatePassword(@RequestParam String password) {
        return ResponseEntity.ok()
                .body(BaseResponse.build(passwordValidationService.isPasswordBreached(password)));
    }
}
