package com.example.shop.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class TokenUtils {
    public static String extractToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Authorization token is missing");
        }
        return token;
    }
}
