package com.example.jwt.repositories;

import com.example.jwt.entities.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
