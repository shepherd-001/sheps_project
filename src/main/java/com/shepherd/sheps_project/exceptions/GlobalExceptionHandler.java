package com.shepherd.sheps_project.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Builder(toBuilder = true)
    public static class ApiError {
        private final String message;
        @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
        private final LocalDateTime timestamp;
        private final boolean status;
    }

    private static ApiError errorResponseBuilder(String message){
        return ApiError.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .status(false)
                .build();
    }

    @ExceptionHandler(ShepsException.class)
    public ResponseEntity<ApiError> handleShepsException(ShepsException ex) {
        log.info("\nSheps exception {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiError> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        log.info("\nUnsupported exception {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.info("\nData integrity violation exception {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFounException(ResourceNotFoundException ex) {
        log.info("\nResource not found exception {}\n", ex.getMessage());
        return new ResponseEntity<>(errorResponseBuilder(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
