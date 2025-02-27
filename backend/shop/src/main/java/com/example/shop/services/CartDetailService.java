package com.example.shop.services;

import com.example.shop.dtos.reponse.cart_detail.CartDetailCreateReponse;
import com.example.shop.dtos.reponse.client.ClientReponse;
import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.client.UserResponse;
import com.example.shop.dtos.reponse.product.ProductCreateReponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.cart.CartDetailRequest;
import com.example.shop.dtos.request.client.TokenData;
import com.example.shop.entities.CartDetail;
import com.example.shop.entities.Product;
import com.example.shop.repositories.CartDetailRepository;
import com.example.shop.repositories.ClientRepository;
import com.example.shop.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartDetailService {

    ClientRepository clientRepository;
    ProductRepository productRepository;
    CartDetailRepository cartDetailRepository;
    Environment environment;

    public CartDetailCreateReponse create(String token, CartDetailRequest request) {
        RestResponse<UserResponse> user = clientRepository.getClientCurrent(token, request.getClientId());
        Product product = productRepository.findById(Long.valueOf(request.getProductId())).orElse(null);
        product.setImage(environment.getProperty("image_url") + product.getImage());

        ClientReponse clientReponse = ClientReponse.builder()
                .id(user.getData().getId())
                .email(user.getData().getEmail())
                .age(user.getData().getAge())
                .gender(user.getData().getGender())
                .address(user.getData().getAddress())
                .createdAt(user.getData().getCreatedAt())
                .build();

        CartDetail cartDetail = new CartDetail();
        cartDetail.setQuantity(request.getQuantity());
        cartDetail.setTotal(request.getQuantity() * product.getPrice().intValue());
        cartDetail.setUser_id(request.getClientId());
        cartDetail.setProduct(product);

        cartDetailRepository.save(cartDetail);

        return CartDetailCreateReponse.builder()
                .id(cartDetail.getId())
                .total(cartDetail.getTotal())
                .quantity(request.getQuantity())
                .created(cartDetail.getCreated_at())
                .client(clientReponse)
                .product(product)
                .build();

    }


    public CartDetailCreateReponse getCartById(String token, int id) {
        TokenData tokenRequest = new TokenData();
        tokenRequest.setToken(token.trim());
        RestResponse<UserInsideTokenResponse> user = clientRepository.getClientCurrent(token,tokenRequest);

        System.out.println(">>>>>>>>>>>>>>>>>>> " + user);
//        Product product = productRepository.findById(Long.valueOf(request.getProductId())).orElse(null);
//        product.setImage(environment.getProperty("image_url") + product.getImage());
//
//        ClientReponse clientReponse = ClientReponse.builder()
//                .id(user.getData().getId())
//                .email(user.getData().getEmail())
//                .age(user.getData().getAge())
//                .gender(user.getData().getGender())
//                .address(user.getData().getAddress())
//                .createdAt(user.getData().getCreatedAt())
//                .build();
//
//        CartDetail cartDetail = new CartDetail();
//        cartDetail.setQuantity(request.getQuantity());
//        cartDetail.setTotal(request.getQuantity() * product.getPrice().intValue());
//        cartDetail.setUser_id(request.getClientId());
//        cartDetail.setProduct(product);
//

//        return CartDetailCreateReponse.builder()
//                .id(cartDetail.getId())
//                .total(cartDetail.getTotal())
//                .quantity(request.getQuantity())
//                .created(cartDetail.getCreated_at())
//                .client(clientReponse)
//                .product(product)
//                .build();

        return null;

    }
}
