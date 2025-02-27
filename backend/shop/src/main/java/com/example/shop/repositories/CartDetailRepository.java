package com.example.shop.repositories;

import com.example.shop.entities.CartDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CartDetailRepository extends CrudRepository<CartDetail, Long>,
        PagingAndSortingRepository<CartDetail, Long> {

}
