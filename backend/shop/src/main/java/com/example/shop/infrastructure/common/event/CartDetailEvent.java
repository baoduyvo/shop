package com.example.shop.infrastructure.common.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CartDetailEvent {

    private String cartDetailNumber;
    private Set<ProductInsideEvent> productInsideEvent;

    @Getter
    @Setter
    public static class ProductInsideEvent {
        private Long productId;
        private Integer quantity;
        private Double price;
        private String productName;
        private int totalPrice;

    }
}
