package com.shepherd.sheps_project.controllers;

import com.shepherd.sheps_project.controllers.responses.BaseResponse;
import com.shepherd.sheps_project.services.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/userdetails/id")
    public ResponseEntity<Object> getUserById(@RequestParam String userId){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getUserById(userId)));
    }
    @GetMapping("/userdetails/email")
    public ResponseEntity<Object> getUserByEmail(@RequestParam String userEmail){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getUserByEmail(userEmail)));
    }
}