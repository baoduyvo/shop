package com.example.jwt.dtos.request.user;

import com.example.jwt.utils.constants.GenderEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    int age;
    GenderEnum gender;
    String address;
}
