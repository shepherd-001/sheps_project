package com.shepherd.sheps_project.data.dtos.requests;

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
    @NotBlank(message = "First name is required")
    @Pattern(message = "First name can only contain letters, apostrophes, and hyphens," +
            " but cannot start or end with apostrophes or hyphens", regexp = NAME)
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Pattern(message = "Last name can only contain letters, apostrophes, and hyphens," +
            " but cannot start or end with apostrophes or hyphens", regexp = NAME)
    private String lastName;
    @NotBlank(message = "Email address is required")
    @Email(regexp = EMAIL, message = "Email address is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = PASSWORD, message = "Password must be between 8 and 16 characters" +
    " long and include at least one lowercase letter, one uppercase letter, one number," +
    " and one special character (e.g., @, #, $, %, ^, &, +, =, !, ...)")
    private String password;
    @NotNull(message = "Gender is required")
    private String gender;
    @NotNull(message = "Role is required")
    private String role;
}
