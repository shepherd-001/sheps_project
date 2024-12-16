package com.shepherd.sheps_project.data.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email address is required")
    @Email(regexp = "", message = "Email address is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 16, message = "Password must be contains 8 and 16 characters in length")
    @Pattern(regexp = "", message = "")
    private String password;
    @NotNull(message = "Gender is required")
    private String gender;
    @NotNull(message = "Role is required")
    private String role;
}
