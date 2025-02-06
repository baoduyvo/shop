package com.example.jwt.dtos.response.util;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;
}
