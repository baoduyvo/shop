package com.example.jwt.repositories;

import com.example.jwt.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query("select r from Role r where r.name = :name")
    public Role fetchRoleByName(@Param("name") String name);
}
