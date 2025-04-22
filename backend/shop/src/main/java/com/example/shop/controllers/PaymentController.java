package com.example.shop.controllers;

import com.example.shop.configuration.TokenUtils;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {

    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<RestResponse<Void>> create(HttpServletRequest httpServletRequest) {
        String token = TokenUtils.extractToken(httpServletRequest);

        paymentService.submit(token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Payments Product Successfully")
                        .data(null)
                        .build());
    }
}
