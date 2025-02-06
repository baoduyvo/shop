package com.example.jwt.entities;

import com.example.jwt.services.AuthenticationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    public Permission(String name, String apiPath, String method, String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

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
