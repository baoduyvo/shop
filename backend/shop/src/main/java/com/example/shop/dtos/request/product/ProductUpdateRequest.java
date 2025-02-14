package com.example.shop.dtos.request.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    String Name;

    @Min(value = 1, message = "Price must be greater than 1")
    Double price;

    @Min(value = 1, message = "Quantity must be greater than 1")
    @Max(value = 100, message = "Quantity must be less than 100")
    Integer quantity;

    String description;

    boolean acctive;

    int categoryId;
}
