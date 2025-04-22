package com.example.shop.repositories;
import org.springframework.data.domain.Pageable;
import com.example.shop.entities.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface CartDetailRepository extends CrudRepository<CartDetail, Long>,
        PagingAndSortingRepository<CartDetail, Long> {

    @Query("select c from CartDetail c where c.user_id = :user_id")
    Page<CartDetail> findByUserId(@Param("user_id") String user_id, Pageable pageable);
}
