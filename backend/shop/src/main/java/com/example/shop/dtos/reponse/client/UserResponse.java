package com.example.shop.dtos.reponse.client;

import com.example.shop.utils.constants.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String password;
    int age;
    GenderEnum gender;
    String address;
    Instant createdAt;
}
