package com.example.shop.services;

import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.dtos.reponse.product.ProductUpdateReponse;
import com.example.shop.dtos.reponse.utils.Pagination;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.product.ProductCreateRequest;
import com.example.shop.dtos.request.product.ProductUpdateRequest;
import com.example.shop.entities.Category;
import com.example.shop.entities.Product;
import com.example.shop.exception.error.IDException;
import com.example.shop.repositories.CategoryRepository;
import com.example.shop.repositories.ProductRepository;
import com.example.shop.utils.FileHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    Environment environment;

    public ProductCreateReponse create(MultipartFile file, ProductCreateRequest request) throws IOException {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setImage(file.isEmpty() ? "default.png" : saveImage(file));
        product.setActive(request.isAcctive());
        product.setCategory(categoryRepository.findById(Long.valueOf(request.getCategoryId())).orElse(null));

        product = productRepository.save(product);

        return ProductCreateReponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(environment.getProperty("image_url") + product.getImage())
                .description(product.getDescription())
                .acctive(product.isActive())
                .category(product.getCategory())
                .created(product.getCreatedAt())
                .build();
    }

    public ProductUpdateReponse update
            (Long id,
             MultipartFile file,
             ProductUpdateRequest request) throws IOException {

        Product product = productRepository.findById(id).orElse(null);
        updateProductFields(product, request);

        if (file != null) {
            if ("default.png".equalsIgnoreCase(product.getImage()) || deleteImage(product.getImage())) {
                product.setImage(saveImage(file));
            }
        }

        product = productRepository.save(product);
        return ProductUpdateReponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(environment.getProperty("image_url") + product.getImage())
                .description(product.getDescription())
                .acctive(product.isActive())
                .category(product.getCategory())
                .updated(product.getUpdatedAt())
                .build();
    }

    private void updateProductFields(Product product, ProductUpdateRequest request) {
        Optional.ofNullable(request.getName()).filter(name -> !name.isEmpty()).ifPresent(product::setName);
        Optional.ofNullable(request.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(request.getQuantity()).ifPresent(product::setQuantity);
        Optional.ofNullable(request.getDescription()).filter(desc -> !desc.isEmpty()).ifPresent(product::setDescription);
        Optional.ofNullable(request.isAcctive()).ifPresent(product::setActive);

        if (Integer.valueOf(request.getCategoryId()) != null) {
            Category category = categoryRepository.findById(Long.valueOf(request.getCategoryId()))
                    .orElseThrow(() -> new IDException("Category not found"));
            product.setCategory(category);
        }
    }

    public boolean delete(long id) {
        try {
            Product product = productRepository.findById(id).orElse(null);
            if ("default.png".equalsIgnoreCase(product.getImage()) || deleteImage(product.getImage())) {
                productRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Delete Product Failed", e);
        }
    }

    public ProductCreateReponse getByIdProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        return ProductCreateReponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(environment.getProperty("image_url") + product.getImage())
                .description(product.getDescription())
                .acctive(product.isActive())
                .category(product.getCategory())
                .created(product.getCreatedAt())
                .build();
    }

    public ProductCreateReponse getByNameProduct(String name) {
        Product product = productRepository.findByNameProduct(name);

        return ProductCreateReponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(environment.getProperty("image_url") + product.getImage())
                .description(product.getDescription())
                .acctive(product.isActive())
                .category(product.getCategory())
                .created(product.getCreatedAt())
                .build();
    }

    public boolean exitByIdProduct(Long id) {
        return productRepository.existsById(id);
    }

    public boolean exitByNameProduct(String name) {
        return productRepository.existsByNameProduct(name);
    }

    private boolean deleteImage(String image) {
        try {
            File file = new File("D:/java/project/backend/shop/src/main/resources/static/images" + File.separator + image);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
                return true;
            } else {
                System.out.println("Delete operation is failed.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to Delete image !!");
            return false;
        }
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        System.out.println("File Info");
        System.out.println("name: " + imageFile.getOriginalFilename());
        System.out.println("size: " + imageFile.getSize());
        System.out.println("type: " + imageFile.getContentType());

        String fileName = FileHelper.generateFileName(imageFile.getOriginalFilename());
        Path path = Paths.get("D:/java/project/backend/shop/src/main/resources/static/images" + File.separator + fileName);
        Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public ResultPaginationDTO fillAll(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setResult(productsPage.getContent().stream()
                .map(product -> ProductCreateReponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .imageUrl(environment.getProperty("image_url") + product.getImage())
                        .description(product.getDescription())
                        .acctive(product.isActive())
                        .category(product.getCategory())
                        .created(product.getCreatedAt())
                        .build())
                .collect(Collectors.toList()));

        resultPaginationDTO.setPagination(Pagination.builder()
                .totalPages(productsPage.getTotalPages())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalData(productsPage.getTotalElements())
                .build());

        return resultPaginationDTO;
    }
}
