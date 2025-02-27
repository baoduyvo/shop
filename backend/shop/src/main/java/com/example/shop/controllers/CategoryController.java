package com.example.shop.controllers;

import com.example.shop.dtos.reponse.category.CategoryCreateReponse;
import com.example.shop.dtos.reponse.category.CategoryUpdateReponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.category.CategoryRequest;
import com.example.shop.exception.error.IDException;
import com.example.shop.exception.error.NameException;
import com.example.shop.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<RestResponse<CategoryCreateReponse>> create(@Valid @RequestBody CategoryRequest request) throws NameException {
        if (categoryService.exitByNameCategory(request.getName()))
            throw new NameException("Name Category already exists");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CategoryCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.create(request))
                        .message("Created Category Successfully")
                        .build());
    }

    @GetMapping
    public ResponseEntity<RestResponse<ResultPaginationDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "size", defaultValue = "10") String size) {
        try {
            int currentPage = Integer.parseInt(page) - 1;
            int pageSize = Integer.parseInt(size);
            Pageable pageable = PageRequest.of(currentPage, pageSize);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(RestResponse.<ResultPaginationDTO>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Get Category With Panigation Successfully")
                            .data(categoryService.fillAllCategory(pageable))
                            .build());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format for input: ");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestResponse<CategoryUpdateReponse>> update(
            @PathVariable("id") long id,
            @Valid @RequestBody CategoryRequest request) throws IDException {
        if (!categoryService.exitByIdCategory(id))
            throw new IDException("ID Category already exists");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CategoryUpdateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.update(id, request))
                        .message("Updated Category Successfully")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<String>> delete(@PathVariable("id") long id) throws IDException {
        if (!categoryService.exitByIdCategory(id))
            throw new IDException("ID Category already exists");

        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Category Successfully")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<CategoryCreateReponse>> getCategorById(@PathVariable("id") long id) throws IDException {
        if (!categoryService.exitByIdCategory(id))
            throw new IDException("ID Category already exists");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CategoryCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Category By ID Successfully")
                        .data(categoryService.getCategoryById(id))
                        .build());
    }

    @PostMapping("/search")
    public ResponseEntity<RestResponse<CategoryCreateReponse>> getCategorByName(@RequestParam("name") String name) throws NameException {
        if (!categoryService.exitByNameCategory(name))
            throw new NameException("Name Category already exists");

        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.<CategoryCreateReponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Category By Name Successfully")
                        .data(categoryService.getCategoryByName(name))
                        .build());
    }
}
