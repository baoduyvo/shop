package com.example.shop.services;

import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.utils.Pagination;
import com.example.shop.dtos.reponse.utils.ResultPaginationDTO;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.entities.CartDetail;
import com.example.shop.entities.Product;
import com.example.shop.exception.error.IDException;
import com.example.shop.infrastructure.common.event.CartDetailEvent;
import com.example.shop.infrastructure.order.CreateCartProducer;
import com.example.shop.repositories.CartDetailRepository;
import com.example.shop.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {

    ProductRepository productRepository;
    CartDetailRepository cartDetailRepository;
    CreateCartProducer createCartProducer;

    BaseRedisService baseRedisService;
    ClientService clientService;

    private static final String CART = "CART_DETAIL:";
    private static final String PRODUCT = "PRODUCT:";

    public void submit(String token) throws IDException {
        UserInsideTokenResponse getUserInside = clientService.getClientIntrospect(token);
        Map<String, Object> hashKey = baseRedisService.getAllFromHash(CART + getUserInside.getId());

        CartDetailEvent cartDetailEvent = new CartDetailEvent();

        Set<CartDetailEvent.ProductInsideEvent> cartDetails = hashKey.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(PRODUCT))
                .map(entry -> {
                    CartDetailEvent.ProductInsideEvent productInsideEvent = new CartDetailEvent.ProductInsideEvent();
                    String productIdStr = entry.getKey().substring(PRODUCT.length());
                    Long productId = Long.valueOf(productIdStr);
                    int quantity = (int) entry.getValue();

                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IDException("Product not found with ID: " + productId));
                    productInsideEvent.setProductName(product.getName());
                    productInsideEvent.setProductId(product.getId());
                    productInsideEvent.setPrice(product.getPrice());
                    productInsideEvent.setQuantity(product.getQuantity());
                    productInsideEvent.setTotalPrice(product.getPrice().intValue() * quantity);

                    return productInsideEvent;
                })
                .collect(Collectors.toSet());

        cartDetailEvent.setCartDetailNumber(CART + getUserInside.getId());
        cartDetailEvent.setProductInsideEvent(cartDetails);

        createCartProducer.sendMessage(cartDetailEvent);
    }

}
