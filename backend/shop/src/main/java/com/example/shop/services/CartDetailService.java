package com.example.shop.services;

import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.utils.Pagination;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.entities.CartDetail;
import com.example.shop.entities.Product;
import com.example.shop.exception.error.IDException;
import com.example.shop.repositories.CartDetailRepository;
import com.example.shop.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartDetailService {

    ProductRepository productRepository;
    CartDetailRepository cartDetailRepository;

    BaseRedisService baseRedisService;
    ClientService clientService;

    private static final String CART = "CART_DETAIL:";
    private static final String PRODUCT = "PRODUCT:";

    public void create(String token, List<CartDetailRequest> request) throws IDException {
        UserInsideTokenResponse getUserInside = clientService.getClientIntrospect(token);

        for (CartDetailRequest r : request) {
            Long productId = Long.valueOf(r.getProductId());

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IDException("Product ID Not Found"));

            int totalPrice = r.getQuantity() * product.getPrice().intValue();
            baseRedisService.addToHash(CART + getUserInside.getId(),
                    PRODUCT + productId,
                    r.getQuantity());

            CartDetail cartDetail = CartDetail.builder()
                    .quantity(r.getQuantity())
                    .total(totalPrice)
                    .user_id(getUserInside.getId())
                    .product(product)
                    .build();

            cartDetailRepository.save(cartDetail);
        }
    }

    public ResultPaginationDTO fillAll(String token, Pageable pageable) {
        UserInsideTokenResponse getUserInside = clientService.getClientIntrospect(token);

        Page<CartDetail> cartDetailsPage = cartDetailRepository.findByUserId(getUserInside.getId(), pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();

        resultPaginationDTO.setResult(cartDetailsPage.getContent().stream()
                .map(cart -> CartDetail.builder()
                        .id(cart.getId())
                        .quantity(cart.getQuantity())
                        .total(cart.getTotal())
                        .user_id(getUserInside.getId())
                        .product(productRepository.findById(cart.getProduct().getId())
                                .orElseThrow(() -> new IDException("Product Not Found")))
                        .created_at(cart.getCreated_at())
                        .build())
                .collect(Collectors.toList()));

        resultPaginationDTO.setPagination(Pagination.builder()
                .totalPages(cartDetailsPage.getTotalPages())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalData(cartDetailsPage.getTotalElements())
                .build());

        return resultPaginationDTO;
    }


}
