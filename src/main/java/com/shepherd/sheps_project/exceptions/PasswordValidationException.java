package com.shepherd.sheps_project.exceptions;


import lombok.Getter;
@Getter
public class PasswordValidationException extends ShepsException {
    private final int statusCode;

    public PasswordValidationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
