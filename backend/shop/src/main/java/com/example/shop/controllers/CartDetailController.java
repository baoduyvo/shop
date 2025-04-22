package com.example.shop.controllers;

import com.example.shop.configuration.TokenUtils;
import com.example.shop.dtos.reponse.cart_detail.CartDetailCreateReponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.services.CartDetailService;
import com.example.shop.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartDetailController {
    ProductService productService;
    CartDetailService cartDetailService;

    Environment environment;

    @PostMapping
    public ResponseEntity<RestResponse<Void>> create(
            HttpServletRequest httpServletRequest,
            @RequestBody List<CartDetailRequest> request) {
        String token = TokenUtils.extractToken(httpServletRequest);

        cartDetailService.create(token, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Created Cart Detail Successfully")
                        .data(null)
                        .build());
    }

    @GetMapping
    public ResponseEntity<RestResponse<ResultPaginationDTO>> findAll(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "size", defaultValue = "5") String size) throws NumberFormatException {
        try {
            String token = TokenUtils.extractToken(httpServletRequest);

            int currentPage = Integer.parseInt(page) - 1;
            int pageSize = Integer.parseInt(size);
            Pageable pageable = PageRequest.of(currentPage, pageSize);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(RestResponse.<ResultPaginationDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Get Cart Detail With Panigation Successfully")
                            .data(cartDetailService.fillAll(token, pageable))
                            .build());

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format for input: ");
        }
    }
}
