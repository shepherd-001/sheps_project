package com.shepherd.sheps_project.data.dtos.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestPasswordResetResponse {
    private String message;
}