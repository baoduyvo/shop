package com.example.shop.dtos.request.product;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {

    @NotEmpty(message = "{NotEmpty}")
    String Name;

    @Min(value = 1, message = "Price must be greater than 1")
    Double price;

    @Min(value = 1, message = "Quantity must be greater than 1")
    @Max(value = 100, message = "Quantity must be less than 100")
    Integer quantity;

    @NotEmpty(message = "{NotEmpty}")
    String description;

    boolean acctive;

    int categoryId;
}
