package com.shepherd.sheps_project.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BaseResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private boolean isSuccessful;
    @JsonFormat(pattern = "HH:mm:ss, dd-MM-yyyy")
    private LocalDateTime timeStamp;


    public static BaseResponse<Object> build(Object data){
        return BaseResponse.builder()
                .data(data)
                .isSuccessful(true)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static BaseResponse<Object> build(String message){
        return BaseResponse.builder()
                .message(message)
                .isSuccessful(true)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
