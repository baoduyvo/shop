package com.example.shop.dtos.reponse.client;

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
