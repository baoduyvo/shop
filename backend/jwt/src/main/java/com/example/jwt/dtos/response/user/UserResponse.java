package com.example.jwt.dtos.response.user;

import com.example.jwt.utils.constants.GenderEnum;
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
