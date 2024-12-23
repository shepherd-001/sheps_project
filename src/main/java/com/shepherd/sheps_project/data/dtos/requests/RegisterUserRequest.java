package com.shepherd.sheps_project.data.dtos.requests;

import com.shepherd.sheps_project.utils.ValidationMessage;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.shepherd.sheps_project.utils.RegexPattern.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = ValidationMessage.BLANK_FIRST_NAME)
    @Pattern(message = ValidationMessage.INVALID_FIRST_NAME, regexp = USER_NAME)
    private String firstName;

    @NotBlank(message = ValidationMessage.BLANK_LAST_NAME)
    @Pattern(message = ValidationMessage.INVALID_LAST_NAME, regexp = USER_NAME)
    private String lastName;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Email(regexp = EMAIL, message = ValidationMessage.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
    @Pattern(regexp = PASSWORD, message = ValidationMessage.INVALID_PASSWORD)
    private String password;

    @NotBlank(message = ValidationMessage.BLANK_GENDER)
    private String gender;
    @NotBlank(message = ValidationMessage.BLANK_ROLE)
    private String role;
}
