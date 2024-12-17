package com.shepherd.sheps_project.services.emailService;
import java.util.Map;


public interface EmailValidationService {
    Map<String, Object> validateEmail(String email);
    boolean isDisposableEmail(String email);
}
