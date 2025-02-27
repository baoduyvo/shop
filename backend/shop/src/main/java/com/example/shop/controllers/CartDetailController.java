package com.example.shop.controllers;

import com.example.shop.dtos.reponse.cart_detail.CartDetailCreateReponse;
import com.example.shop.dtos.reponse.client.ClientReponse;
import com.example.shop.dtos.reponse.client.UserResponse;
import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.exception.error.IDException;
import com.example.shop.services.CartDetailService;
import com.example.shop.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartDetailController {
    ProductService productService;
    CartDetailService cartDetailService;
    Environment environment;

    // Tạo giỏ hàng mới
    // Lấy thông tin giỏ hàng theo ID
    // Thêm sản phẩm vào giỏ hàng
    // Xóa sản phẩm khỏi giỏ hàng
    // Cập nhật số lượng sản phẩm trong giỏ hàng
    // Xóa giỏ hàng
    // Lấy tất cả sản phẩm trong giỏ hàng
    // Tính tổng giá trị giỏ hàng

    @PostMapping
    public ResponseEntity<RestResponse<CartDetailCreateReponse>> create(
            HttpServletRequest httpServletRequest,
            @RequestBody CartDetailRequest request) {

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        CartDetailCreateReponse cart = cartDetailService.create(token, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CartDetailCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Created Cart Detail With User Has ID: " + request.getClientId() + " Successfully")
                        .data(cart)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<CartDetailCreateReponse>> getCartById(
            HttpServletRequest httpServletRequest,
            @PathVariable("id") int id) throws IDException {
//        if (!productService.exitByIdProduct(Long.valueOf(id)))
//            throw new IDException("Product not found");
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        cartDetailService.getCartById(token, id);


        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CartDetailCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Cart Detail By ID Successfully")
                        .build());
    }
}
