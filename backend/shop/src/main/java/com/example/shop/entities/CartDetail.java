package com.example.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "cart_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetail implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Integer quantity;
        private Integer total;
        private String user_id;

        @Column(name = "created_at", columnDefinition = "DATETIME")
        private Instant created_at;

        @Column(name = "updated_at", columnDefinition = "DATETIME")
        private Instant updated_at;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

    @PrePersist
    public void handleBeforeCreate() {
        this.created_at = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updated_at = Instant.now();
    }
}
