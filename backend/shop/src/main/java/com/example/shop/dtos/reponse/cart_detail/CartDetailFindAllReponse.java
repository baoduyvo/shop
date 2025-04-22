package com.example.shop.dtos.reponse.cart_detail;

import com.example.shop.entities.CartDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailFindAllReponse {
    Integer subTotal;
    Set<CartDetail> cartDetails;

}
