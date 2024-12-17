package com.shepherd.sheps_project.services;

import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;
import com.shepherd.sheps_project.data.models.Gender;
import com.shepherd.sheps_project.data.models.Role;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.*;
import com.shepherd.sheps_project.services.emailService.EmailValidationService;
import com.shepherd.sheps_project.services.passwordServie.PasswordValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        checkIfUserExists(email);
        validateEmail(email);
        validatePassword(registrationRequest.getPassword());
        User newUser = buildRegisterUserRequest(registrationRequest);
        User savedUser = userRepository.save(newUser);
        log.info("User with the first name {} registered successfully", savedUser.getFirstName());
//        todo implement send email verification
        return buildRegisterUserResponse(savedUser);
    }

    private void checkIfUserExists(String email) {
        if(userRepository.existsByEmail(email))
            throw new AlreadyExistsException("User with the provided email already exists");
    }

    private void validateEmail(String email) {
//        Map<String, Object> validationResponse = emailValidationService.validateEmail(email);
//
//        if ("valid".equals(validationResponse.get("status"))) {
//            log.info("The email address is valid");
//        } else if ("disposable".equals(validationResponse.get("sub_status"))
//                || emailValidationService.isDisposableEmail(email)) {
//            log.warn("The email address entered is disposable");
//            throw new DisposableEmailException("Disposable email is not allowed");
//        } else {
//            log.error("Error validating email address");
//            throw new EmailValidationException("Error validating email address. Try again with a valid email address");
//        }
    }

    private void validatePassword(String password){
        if(passwordValidationService.isPasswordBreached(password))
            throw new PasswordValidationException("This password has been compromised. Use a new, unique password"
                    , BAD_REQUEST.value());
    }

    private static User buildRegisterUserRequest(RegisterUserRequest registrationRequest) {
        return User.builder()
                .firstName(registrationRequest.getFirstName().trim())
                .lastName(registrationRequest.getLastName().trim())
                .email(registrationRequest.getEmail().toLowerCase().trim())
                .password(registrationRequest.getPassword())
                .gender(Gender.valueOf(registrationRequest.getGender().toUpperCase().trim()))
                .roles(Set.of(Role.valueOf(registrationRequest.getRole().toUpperCase().trim())))
                .build();
    }

    private static RegisterUserResponse buildRegisterUserResponse(User savedUser) {
        return RegisterUserResponse.builder()
                .message("User registered successfully")
                .userId(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .roles(savedUser.getRoles())
                .isEnabled(savedUser.isEnabled())
                .build();
    }
}
