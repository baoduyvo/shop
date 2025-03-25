package com.example.shop.services;

import com.example.shop.dtos.reponse.cart_detail.CartDetailCreateReponse;
import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.dtos.reponse.utils.Pagination;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.entities.CartDetail;
import com.example.shop.entities.Product;
import com.example.shop.exception.error.IDException;
import com.example.shop.repositories.CartDetailRepository;
import com.example.shop.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final String USERID_KEY = "USERID_";
    private static final String CART_KEY = "CART_DETAIL_KEY_";

    public CartDetailCreateReponse create(String token, CartDetailRequest request) throws IDException {
        Product product = productRepository.findById(Long.valueOf(request.getProductId()))
                .orElseThrow(() -> new IDException("ID Product Not Found"));

        UserInsideTokenResponse getUserInside = clientService.getClientIntrospect(token);

        CartDetail cartDetail = CartDetail.builder()
                .quantity(request.getQuantity())
                .total(request.getQuantity() * product.getPrice().intValue())
                .user_id(getUserInside.getId())
                .product(product)
                .build();

        cartDetailRepository.save(cartDetail);

        String userIdKey = USERID_KEY + getUserInside.getId();
        String cartDetailKey = CART_KEY + cartDetail.getId();

        if (!baseRedisService.hasKey(userIdKey) || baseRedisService.getDataWithKey(cartDetailKey, CartDetail.class) == null) {
            Map<String, Object> listCartDetail = new HashMap<>();
            log.info("Thêm CartDetail vào mảng giỏ hàng của user: {}", userIdKey);
            listCartDetail.put(cartDetailKey, cartDetail);
            baseRedisService.addToList(userIdKey, listCartDetail);
        }

        return CartDetailCreateReponse.builder()
                .id(cartDetail.getId())
                .total(cartDetail.getTotal())
                .quantity(request.getQuantity())
                .created(cartDetail.getCreated_at())
                .product(product)
                .build();

    }

    public ResultPaginationDTO fillAll(String token, Pageable pageable) {
        UserInsideTokenResponse getUserInside = clientService.getClientIntrospect(token);
        List<Object> results = baseRedisService.getListElements(USERID_KEY + getUserInside.getId());

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        List<Object> pageContent = results.subList(start, end);

        Page<List<Object>> cartDetailsPage = new PageImpl<>(List.of(pageContent),
                pageable, results.size());

        List<Map<String, CartDetail>> resultList = cartDetailsPage.getContent().stream()
                .map(cart -> deserializeCartDetails(cart.toString(), objectMapper))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new ResultPaginationDTO(
                Pagination.builder()
                        .totalPages(cartDetailsPage.getTotalPages())
                        .currentPage(pageable.getPageNumber() + 1)
                        .pageSize(pageable.getPageSize())
                        .totalData(cartDetailsPage.getTotalElements())
                        .build(),
                resultList
        );
    }

    private List<Map<String, CartDetail>> deserializeCartDetails(String cartJson, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(cartJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class,
                            objectMapper.getTypeFactory().constructMapType(Map.class, String.class, CartDetail.class)));
        } catch (Exception e) {
            log.error("Error deserializing cart details", e);
            return Collections.emptyList();
        }
    }
}
