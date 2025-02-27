package com.example.apigateway.configuration;

import com.example.apigateway.dtos.reponse.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    ObjectMapper objectMapper;
    @NonFinal
    private String[] publicEndpoints = {
            "/jwt/auth/.*"
    };

    @Value("${server.servlet.context-path}")
    @NonFinal
    private String apiPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        String requestUri = exchange.getRequest().getURI().toString();
        String path = exchange.getRequest().getPath().value();
        String requestMethod = String.valueOf(exchange.getRequest().getMethod());
        String requestHeaders = exchange.getRequest().getHeaders().toString();

        log.info("Request URI: {}", requestUri);
        log.info("Request Path: {}", path);
        log.info("Request Method: {}", requestMethod);
        log.info("Request Headers: {}", requestHeaders);
        log.info("predicates: {}", apiPath);

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> {
                    boolean isMatch = request.getURI().getPath().matches(apiPath + s);
                    if (isMatch) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request.getURI().getPath());
                    } else {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + false);
                    }
                    return isMatch;
                });
    }

    Mono<Void> unauthenticated(ServerHttpResponse response){
        RestResponse<?> apiResponse = RestResponse.builder()
                .statusCode(401)
                .error("Authentication required")
                .message("You must be authenticated to access this resource")
                .data(null)
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
