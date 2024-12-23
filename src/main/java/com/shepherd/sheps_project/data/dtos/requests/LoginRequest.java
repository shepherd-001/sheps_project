package com.shepherd.sheps_project.data.dtos.requests;

import com.shepherd.sheps_project.utils.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.shepherd.sheps_project.utils.RegexPattern.EMAIL;
import static com.shepherd.sheps_project.utils.RegexPattern.PASSWORD;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Email(regexp = EMAIL, message = ValidationMessage.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
    @Pattern(regexp = PASSWORD, message = ValidationMessage.INVALID_PASSWORD)
    private String password;
}
