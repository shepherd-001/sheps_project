package com.shepherd.sheps_project.data.dtos.requests;

import com.shepherd.sheps_project.utils.RegexPattern;
import com.shepherd.sheps_project.utils.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailConfirmationRequest {
    @NotBlank(message = ValidationMessage.BLANK_TOKEN)
    private String token;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Email(regexp = RegexPattern.EMAIL, message = ValidationMessage.INVALID_EMAIL)
    private String email;
}
