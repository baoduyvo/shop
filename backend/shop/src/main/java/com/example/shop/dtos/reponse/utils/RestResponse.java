package com.example.shop.dtos.reponse.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {
    private int statusCode;

    @Builder.Default
    private String error = "999999";
    private Object message;
    private T data;
}
