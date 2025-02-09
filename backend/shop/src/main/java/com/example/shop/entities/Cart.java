package com.example.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String user_id;
    private String address;
    private Integer total;

    private Date created_at;
    private Date updated_at;

    @OneToMany(mappedBy = "cart",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartDetail> cartDetailList;
}
