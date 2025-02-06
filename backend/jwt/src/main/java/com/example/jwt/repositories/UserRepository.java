package com.example.jwt.repositories;

import com.example.jwt.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String>,
        PagingAndSortingRepository<User, String> {

    @Query("select count(u) > 0 from User u where u.email = :email")
    public boolean existsByEmail(@Param("email") String email);

    @Query("select u from User u where u.email = :email")
    public User fetchUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update User u set u.refreshToken = :refreshToken where u.email = :email")
    public int updateRefreshToken(@Param("refreshToken") String refreshToken,
                                  @Param("email") String email);

}
