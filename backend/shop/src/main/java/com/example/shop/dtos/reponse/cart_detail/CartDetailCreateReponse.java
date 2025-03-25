package com.example.shop.dtos.reponse.cart_detail;

import com.example.shop.dtos.reponse.client.ClientReponse;
import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.entities.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailCreateReponse {

    Long id;
    Integer total;
    Integer quantity;
    Instant created;
    Product product;
}
