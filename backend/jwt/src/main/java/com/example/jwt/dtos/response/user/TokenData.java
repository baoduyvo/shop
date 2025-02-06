package com.example.jwt.dtos.response.user;

public class TokenData {
    private String token;  // Assume the JSON has a 'token' field

    // Getters and setters (you can generate using Lombok or manually)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
