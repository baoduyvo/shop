package com.example.shop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "cart_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Integer total;
    private String user_id;

    private Date created_at;
    private Date updated_at;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    public void handleBeforeCreate() {
        this.created_at = new Date();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updated_at = new Date();
    }
}
