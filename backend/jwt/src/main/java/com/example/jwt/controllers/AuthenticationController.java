package com.example.jwt.controllers;

import com.example.jwt.dtos.request.auth.LoginRequest;
import com.example.jwt.dtos.request.user.UserCreateRequest;
import com.example.jwt.dtos.response.AuthenticationResponse;
import com.example.jwt.dtos.response.user.TokenData;
import com.example.jwt.dtos.response.user.UserInsideTokenResponse;
import com.example.jwt.dtos.response.user.UserResponse;
import com.example.jwt.dtos.response.util.RestResponse;
import com.example.jwt.exception.error.EmailException;
import com.example.jwt.exception.error.IDException;
import com.example.jwt.services.AuthenticationService;
import com.example.jwt.services.UserService;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RestResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", authenticationResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(864000)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, resCookies.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(RestResponse.<AuthenticationResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .error("99999")
                        .data(authenticationResponse)
                        .message("Authentication User Successfully")
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Boolean>> logout() throws IDException {

        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(RestResponse.<Boolean>builder()
                        .statusCode(HttpStatus.OK.value())
                        .error("99999")
                        .data(authenticationService.logout())
                        .message("Logout Token User Successfully")
                        .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RestResponse<AuthenticationResponse>> refresh(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
            throws JOSEException, ParseException, IDException {

        if (refresh_token.equals("abc"))
            throw new IDException("Refresh Token is not valid not cookie");

        AuthenticationResponse authenticationResponse = authenticationService.refresh(refresh_token);

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", authenticationResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(864000 / 1000)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, resCookies.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(RestResponse.<AuthenticationResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .error("99999")
                        .data(authenticationResponse)
                        .message("Refresh Token User Successfully")
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) throws EmailException {
        if (userService.existsByEmail(request.getEmail()))
            throw new EmailException("Email already exists");

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(authenticationService.register(request))
                .message("Register User Successfully")
                .build());
    }

    @PostMapping("/introspect")
    public ResponseEntity<RestResponse<UserInsideTokenResponse>> introspect(@RequestBody TokenData tokenRequest)
            throws JOSEException, ParseException {
        String accessToken = tokenRequest.getToken().trim();
        UserInsideTokenResponse insideTokenResponse = authenticationService.introspect(accessToken.trim());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.<UserInsideTokenResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .error("99999")
                        .data(insideTokenResponse)
                        .message("Introspect Token User Successfully")
                        .build());
    }
}
