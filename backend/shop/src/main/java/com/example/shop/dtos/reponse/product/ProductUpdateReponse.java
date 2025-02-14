package com.example.shop.dtos.reponse.product;

import com.example.shop.entities.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateReponse {

    Long id;
    String name;
    Double price;
    float quantity;
    String imageUrl;
    String description;
    boolean acctive;
    Instant updated;

    Category category;
}
