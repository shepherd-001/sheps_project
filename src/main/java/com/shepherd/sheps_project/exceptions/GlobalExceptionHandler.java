package com.shepherd.sheps_project.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Builder
    @Getter
    public static class ApiError {
        private final String message;
        @JsonFormat(pattern = "HH:mm:ss, dd-MM-yyyy")
        private final LocalDateTime timestamp;
        private final boolean status;
        private final Object error;
    }

    private static ApiError errorResponseBuilder(String message){
        return ApiError.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .status(false)
                .build();
    }

    private static ApiError errorResponseBuilder(Object error){
        return ApiError.builder()
                .message("Request validation error")
                .error(error)
                .timestamp(LocalDateTime.now())
                .status(false)
                .build();
    }

    @ExceptionHandler(ShepsException.class)
    public ResponseEntity<ApiError> handleShepsException(ShepsException ex) {
        log.error("\n\nSheps exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiError> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        log.error("\n\nUnsupported exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException ex) {
        log.error("\n\nAlready exists exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("\n\nData integrity violation exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("\n\nResource not found exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailValidationException.class)
    public ResponseEntity<ApiError> handleEmailValidationException(EmailValidationException ex) {
        log.error("\n\nEmail validation exception: {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        log.error("\n\nMethod argument not valid exception: {}\n", ex.getMessage());
        }
        ApiError errorResponse = errorResponseBuilder(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ApiError> handlePasswordValidationException(PasswordValidationException ex) {
        log.error("\n\nPassword validation exception: {}\n", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("\n\nIllegal argument exception: {}\n", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailSenderException.class)
    public ResponseEntity<ApiError> handleException(MailSenderException ex) {
        log.error("Mail sender  exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsAlreadyEnabledException.class)
    public ResponseEntity<ApiError> handleException(UserIsAlreadyEnabledException ex) {
        log.error("User is already enabled exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShepsTokenException.class)
    public ResponseEntity<ApiError> handleException(ShepsTokenException ex) {
        log.error("Sheps token exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleException(AuthenticationException ex) {
        log.error("Login exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleException(UsernameNotFoundException ex) {
        log.error("User name not found exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(BadCredentialsException ex) {
        log.error("Bad credential exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleException(IOException ex) {
        log.error("IO exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiError> handleException(org.springframework.security.core.AuthenticationException ex) {
        log.error("Authentication exception: {}", ex.getMessage());
            return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
