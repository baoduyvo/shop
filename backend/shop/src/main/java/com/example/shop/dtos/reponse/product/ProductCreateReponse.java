package com.example.shop.dtos.reponse.product;

import com.example.shop.entities.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateReponse {

    Long id;
    String name;
    Double price;
    float quantity;
    String imageUrl;
    String description;
    boolean acctive;
    Instant created;

    Category category;
}
