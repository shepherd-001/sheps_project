package com.shepherd.sheps_project.services.emailService;
import com.shepherd.sheps_project.data.dtos.responses.EmailValidationResponse;

import java.util.Map;


public interface EmailValidationService {
    EmailValidationResponse validateEmail(String email);
    boolean isDisposableEmail(String email);
}
