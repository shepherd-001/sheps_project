package com.shepherd.sheps_project.data.dtos.requests;

import com.shepherd.sheps_project.utils.RegexPattern;
import com.shepherd.sheps_project.utils.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResetPasswordRequest {
    @NotBlank(message = ValidationMessage.BLANK_TOKEN)
    private String token;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Email(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
    private String newPassword;
}
