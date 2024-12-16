package com.shepherd.sheps_project.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BaseResponse<T> {
    private String message;
    private boolean isSuccessful;
    private T data;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime timeStamp;


    public static BaseResponse<Object> build(Object data){
        return BaseResponse.builder()
                .message("Operation successful")
                .data(data)
                .isSuccessful(true)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
