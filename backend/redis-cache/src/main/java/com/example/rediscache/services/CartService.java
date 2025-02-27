package com.example.rediscache.services;

import com.example.rediscache.entities.Cart;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {

    private static final String CART_PREFIX = "cart:";
    private RedisTemplate<String, Cart> redisTemplate;

//    public void addItemToCart(String cartId, CartItem item) {
//        Cart cart = getCart(cartId);
//        cart.addItem(item);
//        redisTemplate.opsForValue().set(CART_PREFIX + cartId, cart);
//    }
}
