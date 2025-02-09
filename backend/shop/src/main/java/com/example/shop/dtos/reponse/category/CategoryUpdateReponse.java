package com.example.shop.dtos.reponse.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateReponse {

    long id;
    String name;
    Instant createdAt;
    Instant updateAt;
}
