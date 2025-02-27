package com.example.rediscache.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetail {
    String clientId;
    String productName;
    int productId;
    int quantity;
}
