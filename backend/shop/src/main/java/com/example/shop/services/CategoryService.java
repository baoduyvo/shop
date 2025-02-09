package com.example.shop.services;

import com.example.shop.dtos.reponse.category.CategoryCreateReponse;
import com.example.shop.dtos.reponse.category.CategoryUpdateReponse;
import com.example.shop.dtos.reponse.utils.Pagination;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.category.CategoryRequest;
import com.example.shop.entities.Category;
import com.example.shop.repositories.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;

    public boolean exitByNameCategory(String name) {
        return categoryRepository.existsByNameCategory(name);
    }

    public boolean exitByIdCategory(Long id) {
        return categoryRepository.existsById(id);
    }

    public CategoryCreateReponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);

        return CategoryCreateReponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public CategoryCreateReponse getCategoryByName(String name) {
        Category category = categoryRepository.findByNameCategory(name);

        return CategoryCreateReponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public CategoryCreateReponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category = categoryRepository.save(category);

        return CategoryCreateReponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public CategoryUpdateReponse update(long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElse(null);
        category.setName(request.getName());
        category = categoryRepository.save(category);

        return CategoryUpdateReponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updateAt(category.getUpdatedAt())
                .build();
    }

    public boolean delete(long id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Delete Category Failed", e);
        }
    }

    public ResultPaginationDTO fillAllCategory(Pageable pageable) {
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setResult(categoriesPage.getContent().stream()
                .map(category -> CategoryCreateReponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .createdAt(category.getCreatedAt())
                        .build())
                .collect(Collectors.toList()));

        resultPaginationDTO.setPagination(Pagination.builder()
                .totalPages(categoriesPage.getTotalPages())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalData(categoriesPage.getTotalElements())
                .build());

        return resultPaginationDTO;
    }
}
