package com.shepherd.sheps_project.services.auth;

import com.shepherd.sheps_project.data.dtos.requests.ChangePasswordRequest;
import com.shepherd.sheps_project.data.dtos.requests.LoginRequest;
import com.shepherd.sheps_project.data.dtos.requests.RegisterUserRequest;
import com.shepherd.sheps_project.data.dtos.responses.EmailConfirmationResponse;
import com.shepherd.sheps_project.data.dtos.responses.LoginResponse;
import com.shepherd.sheps_project.data.dtos.responses.PasswordResetResponse;
import com.shepherd.sheps_project.data.dtos.responses.RegisterUserResponse;
import com.shepherd.sheps_project.data.models.*;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.*;
import com.shepherd.sheps_project.services.email.MailSenderService;
import com.shepherd.sheps_project.services.email.EmailValidationService;
import com.shepherd.sheps_project.services.passwordServie.PasswordValidationService;
import com.shepherd.sheps_project.services.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;
    private final MailSenderService mailSenderService;
    private final TokenService tokenService;
    private static final int MAIL_EXPIRATION_TIME = 30;
    private final SpringTemplateEngine templateEngine;


    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        checkIfUserExists(email);
//        validateEmail(email);
//        validatePassword(registrationRequest.getPassword());
        User newUser = buildRegisterUserRequest(registrationRequest);
        User savedUser = userRepository.save(newUser);
        log.info("User with the first name {} registered successfully", savedUser.getFirstName());
        String token = tokenService.createToken(savedUser, TokenType.EMAIL_CONFIRMATION, MAIL_EXPIRATION_TIME);
        sendVerificationMail(savedUser, token);
        return buildRegisterUserResponse(savedUser);
    }

    private void checkIfUserExists(String email) {
        if(userRepository.existsByEmail(email))
            throw new AlreadyExistsException("User with the provided email already exists");
    }

    private void validateEmail(String email) {
        if(!emailValidationService.isValidEmail(email))
            throw new EmailValidationException("Your email address is not acceptable");
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

    private void sendVerificationMail(User user, String token) {
        String verificationLink = String.format("http://localhost:9090/verify?token=%s&email=%s", token, user.getEmail());
        Context context = new Context();
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("confirmationLink", verificationLink);
        String htmlContent = templateEngine.process("email-confirmation", context);
        log.info("Email content ready to be sent to {}", user.getEmail());
        CompletableFuture.runAsync(() -> mailSenderService.sendEmail(user.getEmail(), "Confirm Your Email Address", htmlContent));
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

    @Override
    public EmailConfirmationResponse verifyEmail(String token, String email){
        ShepsToken shepsToken = tokenService.validateToken(token, email, TokenType.EMAIL_CONFIRMATION);
        User user = shepsToken.getUser();
        if(!user.isEnabled()){
            user.setEnabled(true);
            User verifiedUser = userRepository.save(user);
            tokenService.deleteToken(shepsToken);
            return buildEmailConfirmationResponse(verifiedUser);
        }
        throw new UserIsAlreadyEnabledException("User is already verified");
    }

    private static EmailConfirmationResponse buildEmailConfirmationResponse(User user) {
        return EmailConfirmationResponse.builder()
                .message("User verified successfully")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .accessToken("")
                .refreshToken("")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.getEmail());
        if(!user.isEnabled())
            throw new AuthenticationException("Verify your email address before your proceed.");
        else if(user.getPassword().equals(loginRequest.getPassword())){
            return LoginResponse.builder()
                    .message("User logged in successfully")
                    .accessToken("access token")
                    .refreshToken("refresh token")
                    .build();
        }
        throw new AuthenticationException("Invalid email or password");
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User with the provided email not found"));
    }

    @Override
    public PasswordResetResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getUserByEmail(changePasswordRequest.getEmail());
        if(!user.isEnabled())
            throw new AuthenticationException("Verify your email address before your proceed.");
        else if(!user.getPassword().equals(changePasswordRequest.getOldPassword()))
            throw new AuthenticationException("Invalid password");
        else if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword()))
            throw new AuthenticationException("Password do not match");
        else {
            user.setPassword(changePasswordRequest.getNewPassword());
            userRepository.save(user);
            return PasswordResetResponse.builder()
                    .message("Password changed successfully")
                    .build();
        }
    }
}
