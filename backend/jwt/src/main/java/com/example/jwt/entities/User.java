package com.example.jwt.entities;

import com.example.jwt.services.AuthenticationUtils;
import com.example.jwt.utils.constants.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "uers")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;
    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = AuthenticationUtils.getCurrentUserLogin().isPresent() == true
                ? AuthenticationUtils.getCurrentUserLogin().get()
                : "Admin";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = AuthenticationUtils.getCurrentUserLogin().isPresent() == true
                ? AuthenticationUtils.getCurrentUserLogin().get()
                : "Admin";
        this.updatedAt = Instant.now();
    }
}
