package com.shepherd.sheps_project.controllers;

import com.shepherd.sheps_project.controllers.responses.BaseResponse;
import com.shepherd.sheps_project.services.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/authenticated")
    public ResponseEntity<Object> getUserDetails(){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getAuthenticatedUser()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable String userId){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getUserById(userId)));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers(@RequestParam int pageNumber){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getAllUsers(pageNumber)));
    }

    @GetMapping("/all/enabled")
    public ResponseEntity<Object> getAllEnabledUsers(@RequestParam int pageNumber){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getAllEnabledUsers(pageNumber)));
    }

    @GetMapping("/all/disabled")
    public ResponseEntity<Object> getAllDisabledUsers(@RequestParam int pageNumber){
        return ResponseEntity.ok()
                .body(BaseResponse.build(userService.getAllDisabledUsers(pageNumber)));
    }
}
