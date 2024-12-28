package com.shepherd.sheps_project.data.dtos.responses;

import com.shepherd.sheps_project.data.models.Gender;
import com.shepherd.sheps_project.data.models.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Set<Role> roles;
    private boolean isEnabled;
}
