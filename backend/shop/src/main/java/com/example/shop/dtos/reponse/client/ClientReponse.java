package com.example.shop.dtos.reponse.client;

import com.example.shop.utils.constants.GenderEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientReponse {
    String id;
    String email;
    int age;
    GenderEnum gender;
    String address;
    Instant createdAt;
}
