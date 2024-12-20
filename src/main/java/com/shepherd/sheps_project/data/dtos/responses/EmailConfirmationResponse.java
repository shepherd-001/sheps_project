package com.shepherd.sheps_project.data.dtos.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailConfirmationResponse {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean isEnabled;
    private String accessToken;
    private String refreshToken;
}
