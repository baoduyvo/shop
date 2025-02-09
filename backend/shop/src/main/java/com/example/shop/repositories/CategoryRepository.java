package com.example.shop.repositories;

import com.example.shop.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends CrudRepository<Category, Long>,
        PagingAndSortingRepository<Category, Long> {

    @Query("select count(c) > 0 from Category c where c.name = :name")
    public boolean existsByNameCategory(@Param("name") String name);

    @Query("select c from Category c where c.name = :name")
    public Category findByNameCategory(@Param("name") String name);
}
