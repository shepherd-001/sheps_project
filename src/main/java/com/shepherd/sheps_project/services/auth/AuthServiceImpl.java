package com.shepherd.sheps_project.services.auth;

import com.shepherd.sheps_project.security.JwtService;
import com.shepherd.sheps_project.data.dtos.requests.*;
import com.shepherd.sheps_project.data.dtos.responses.*;
import com.shepherd.sheps_project.data.models.*;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.*;
import com.shepherd.sheps_project.services.email.MailNotificationService;
import com.shepherd.sheps_project.services.email.EmailValidationService;
import com.shepherd.sheps_project.services.passwordServie.PasswordValidationService;
import com.shepherd.sheps_project.services.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.shepherd.sheps_project.utils.AppUtils.getCurrentUser;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;
    private final PasswordValidationService passwordValidationService;
    private final MailNotificationService notificationService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private static final int MAIL_EXPIRATION_TIME_IN_MIN = 30;


    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        checkIfUserExists(email);
//        validateEmail(email);
//        validatePassword(registrationRequest.getPassword());
        User newUser = buildRegisterUserRequest(registrationRequest);
        User savedUser = userRepository.save(newUser);
        log.info("User with the first name {} registered successfully", savedUser.getFirstName());
        String token = tokenService.saveToken(savedUser, TokenType.EMAIL_CONFIRMATION, MAIL_EXPIRATION_TIME_IN_MIN);
        notificationService.sendVerificationMail(savedUser, token);
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

    private User buildRegisterUserRequest(RegisterUserRequest request) {
        return User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(Gender.valueOf(request.getGender().toUpperCase().trim()))
                .roles(Set.of(Role.valueOf(request.getRole().toUpperCase().trim())))
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

    @Override
    public EmailConfirmationResponse verifyEmail(EmailConfirmationRequest request){
        ShepsToken shepsToken = tokenService.validateToken(request.getToken(), request.getEmail(), TokenType.EMAIL_CONFIRMATION);
        User user = shepsToken.getUser();
        if(!user.isEnabled()){
            user.setEnabled(true);
            User verifiedUser = userRepository.save(user);
            tokenService.deleteToken(shepsToken);
            String accessToken = jwtService.generateAccessToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getPassword());
            buildAndSaveToken(verifiedUser, accessToken, refreshToken);
            return buildEmailConfirmationResponse(verifiedUser, accessToken, refreshToken);
        }
        throw new UserIsAlreadyEnabledException("User is already verified");
    }

    private void buildAndSaveToken(User user, String accessToken, String refreshToken) {
        ShepsToken shepsToken = ShepsToken.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.JWT)
                .user(user)
                .isRevoked(false)
                .isExpired(false)
                .build();
        tokenService.saveToken(shepsToken);
    }

    private EmailConfirmationResponse buildEmailConfirmationResponse(User user, String accessToken, String refreshToken) {
        return EmailConfirmationResponse.builder()
                .message("User verified successfully")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String userEmail = authentication.getName();
        String accessToken = jwtService.generateAccessToken(userEmail);
        String refreshToken = jwtService.generateRefreshToken(userEmail);
        User user = getUserByEmail(userEmail);
        if(!user.isEnabled())
            throw new AuthenticationException("Verify your email address before your proceed.");
        buildAndSaveToken(user, accessToken, refreshToken);
        return LoginResponse.builder()
                    .message("User logged in successfully")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User with the provided email not found"));
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentUser();
        checkIfCurrentPasswordIsCorrect(changePasswordRequest.getCurrentPassword(), user.getPassword());
        checkIfCurrentAndNewPasswordAreNotTheSame(changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        checkIfTwoPasswordAreTheSame(changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword());
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(savedUser.getEmail());
        String refreshToken = jwtService.generateRefreshToken(savedUser.getEmail());
        buildAndSaveToken(savedUser, accessToken, refreshToken);
        return ChangePasswordResponse.builder()
                .message("Password changed successfully")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void checkIfCurrentPasswordIsCorrect(String currentPassword, String appUserPassword) {
        if(!passwordEncoder.matches(currentPassword, appUserPassword))
            throw new BadCredentialsException("Invalid password");
    }

    private void checkIfCurrentAndNewPasswordAreNotTheSame(String currentPassword, String newPassword){
        if(currentPassword.equals(newPassword))
            throw new BadCredentialsException("New password cannot be the same as old password");
    }

    private void checkIfTwoPasswordAreTheSame(String newPassword, String confirmPassword){
        if(!newPassword.equals(confirmPassword))
            throw new BadCredentialsException("Password do not match");
    }

    @Override
    public ResetPasswordResponse requestPasswordReset(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isEnabled)
                .map(user -> {
                    String token = tokenService.saveToken(user, TokenType.RESET_PASSWORD, MAIL_EXPIRATION_TIME_IN_MIN);
                    notificationService.sendResetPasswordMail(user, token);
                    return getResetPasswordMessage();
                }).orElse(getResetPasswordMessage());
    }

    private static ResetPasswordResponse getResetPasswordMessage(){
        return ResetPasswordResponse.builder()
                .message("We’ve sent a password reset link to your email address. " +
                "Please follow the instructions in the email to reset your password.")
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        ShepsToken shepsToken = tokenService
                .validateToken(request.getToken(), request.getEmail(), TokenType.RESET_PASSWORD);
        User user = shepsToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        tokenService.deleteToken(shepsToken);
        return ResetPasswordResponse.builder()
                .message("Password reset successful.")
                .build();
    }
}
