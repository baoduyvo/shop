package com.example.jwt.dtos.response.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInsideTokenResponse {
    private String id;
    private String email;
    private String role;
}
