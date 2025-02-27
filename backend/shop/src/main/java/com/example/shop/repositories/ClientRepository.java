package com.example.shop.repositories;

import com.example.shop.dtos.reponse.client.UserInsideTokenResponse;
import com.example.shop.dtos.reponse.client.UserResponse;
import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.dtos.request.client.TokenData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "client-service", url = "http://localhost:8080/jwt")
public interface ClientRepository {

    @GetMapping("/users/{id}")
    RestResponse<UserResponse> getClientCurrent(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("id") String clientId);

    @PostMapping("/auth/introspect")
    RestResponse<UserInsideTokenResponse> getClientCurrent(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TokenData tokenRequest);
}


