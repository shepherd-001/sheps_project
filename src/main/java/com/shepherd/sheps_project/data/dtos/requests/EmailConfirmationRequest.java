package com.shepherd.sheps_project.data.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import static com.shepherd.sheps_project.utils.RegexPattern.EMAIL;

@Builder
@Getter
public class EmailConfirmationRequest {
    @NotBlank(message = "Email confirmation token is required")
    private String token;
    @NotBlank(message = "Email address is required")
    @Email(regexp = EMAIL, message = "Email address is invalid")
    private String email;
}
