package com.example.shop.repositories;

import com.example.shop.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long>,
        PagingAndSortingRepository<Product, Long> {

    @Query("select count(p) > 0 from Product p where p.name = :name")
    public boolean existsByNameProduct(@Param("name") String name);

    @Query("select p from Product p where p.name = :name")
    public Product findByNameProduct(@Param("name") String name);
}
