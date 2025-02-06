package com.example.jwt.exception.error;

public class PermissionException extends RuntimeException {
    public PermissionException(String message) {
        super(message);
    }
}
