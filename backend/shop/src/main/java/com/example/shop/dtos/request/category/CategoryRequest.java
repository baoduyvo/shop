package com.example.shop.dtos.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @NotBlank(message = "This Name Category is required.")
    String name;
}
