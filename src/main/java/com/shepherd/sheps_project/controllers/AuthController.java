package com.shepherd.sheps_project.controllers;

import com.shepherd.sheps_project.controllers.responses.BaseResponse;
import com.shepherd.sheps_project.data.dtos.requests.LoginRequest;
import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
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
    public ResponseEntity<Object> verifyEmail(@RequestParam String token, @RequestParam String email) {
        return ResponseEntity.ok().body(authService.verifyEmail(token, email));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }
}
