package com.example.shop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
