package com.example.shop.services;

import com.example.shop.dtos.reponse.client.ClientReponse;
import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.client.UserResponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.client.TokenData;
import com.example.shop.repositories.ClientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClientService {

    ClientRepository clientRepository;

    public ClientReponse getClientReponse(String token, String clientId) {
        RestResponse<UserResponse> user = clientRepository.getClientCurrent(token, clientId);

        return ClientReponse.builder()
                .id(user.getData().getId())
                .email(user.getData().getEmail())
                .age(user.getData().getAge())
                .gender(user.getData().getGender())
                .address(user.getData().getAddress())
                .createdAt(user.getData().getCreatedAt())
                .build();
    }


    public UserInsideTokenResponse getClientIntrospect(String token) {
        TokenData tokenData = new TokenData();
        tokenData.setToken(token.substring(7));

        RestResponse<UserInsideTokenResponse> user = clientRepository.getClientIntrospect(token, tokenData);

        return UserInsideTokenResponse.builder()
                .id(user.getData().getId())
                .email(user.getData().getEmail())
                .role(user.getData().getRole())
                .build();
    }
}
