package com.example.shop.controllers;

import com.example.shop.dtos.reponse.category.CategoryCreateReponse;
import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.dtos.reponse.product.ProductUpdateReponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.product.ProductCreateRequest;
import com.example.shop.dtos.request.product.ProductUpdateRequest;
import com.example.shop.exception.error.IDException;
import com.example.shop.exception.error.NameException;
import com.example.shop.services.CategoryService;
import com.example.shop.services.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductService productService;
    CategoryService categoryService;
// abc
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<ProductCreateReponse>> create(
            @RequestParam(value = "image") MultipartFile file,
            @Valid @RequestPart ProductCreateRequest request) throws IOException, IDException {

        if (!categoryService.exitByIdCategory(Long.valueOf(request.getCategoryId())))
            throw new IDException("Category already exit");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<ProductCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Created Product Successfully")
                        .data(productService.create(file, request))
                        .build());
    }

    @PutMapping(value = "/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<ProductUpdateReponse>> update(
            @PathVariable("id") int id,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @Valid @RequestPart(value = "request", required = false) ProductUpdateRequest request) throws IOException, IDException {

        if (!productService.exitByIdProduct(Long.valueOf(id)))
            throw new IDException("Product not found");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<ProductUpdateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Updated Product Successfully")
                        .data(productService.update(Long.valueOf(id), file, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<ProductCreateReponse>> delete(@PathVariable("id") int id) throws IDException {
        if (!productService.exitByIdProduct(Long.valueOf(id)))
            throw new IDException("Product not found");

        productService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<ProductCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Product Successfully")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ProductCreateReponse>> getCategorById(@PathVariable("id") int id) throws IDException {
        if (!productService.exitByIdProduct(Long.valueOf(id)))
            throw new IDException("Product not found");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<ProductCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Product By ID Successfully")
                        .data(productService.getByIdProduct(Long.valueOf(id)))
                        .build());
    }

    @PostMapping("/search")
    public ResponseEntity<RestResponse<ProductCreateReponse>> getProductByName(@RequestParam("name") String name) throws NameException {
        if (!productService.exitByNameProduct(name))
            throw new NameException("Name Product already exists");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<ProductCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Product By ID Successfully")
                        .data(productService.getByNameProduct(name))
                        .build());
    }
}
