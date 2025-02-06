package com.example.jwt.services;

import com.example.jwt.dtos.request.auth.LoginRequest;
import com.example.jwt.dtos.request.user.UserCreateRequest;
import com.example.jwt.dtos.response.AuthenticationResponse;
import com.example.jwt.dtos.response.user.UserInsideTokenResponse;
import com.example.jwt.dtos.response.user.UserResponse;
import com.example.jwt.entities.User;
import com.example.jwt.exception.error.IDException;
import com.example.jwt.repositories.RoleRepository;
import com.example.jwt.repositories.UserRepository;
import com.example.jwt.utils.constants.PredefinedRole;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserService userService;
    UserRepository userRepository;
    AuthenticationUtils authenticationUtils;
    AuthenticationManagerBuilder authenticationManagerBuilder;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthenticationResponse authenticate(LoginRequest request) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = userService.fetchUserByEmail(request.getEmail());

        AuthenticationResponse userToken = new AuthenticationResponse();
        AuthenticationResponse.UserLogin userLogin = new AuthenticationResponse.UserLogin();

        if (user != null) {
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setRole(user.getRole().getName());
            userToken.setUser(userLogin);
        }

        String token = authenticationUtils.generateToken(userToken);
        userToken.setAccessToken(token);
        userToken.setExpiryTime(new Date(
                Instant.now().plus(refreshTokenExpiration, ChronoUnit.SECONDS).toEpochMilli()));

        String refreshToken = authenticationUtils.refresh(userLogin);
        userRepository.updateRefreshToken(refreshToken, user.getEmail());
        userToken.setRefreshToken(refreshToken);

        return userToken;
    }

    public boolean logout() throws IDException {
        String email = authenticationUtils.getCurrentUserLogin().isPresent()
                ? authenticationUtils.getCurrentUserLogin().get()
                : "";

        if (email.equals(""))
            throw new IDException("Accsess Token Not Founds");

        User user = userService.fetchUserByEmail(email);

        userRepository.updateRefreshToken("", user.getEmail());

        return true;
    }

    public AuthenticationResponse refresh(String refresh_token) throws JOSEException, ParseException {

        SignedJWT signedJWT = authenticationUtils.verifyToken(refresh_token, true);

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        String userEmail = claimsSet.getJSONObjectClaim("user").get("email").toString();

        User user = userService.fetchUserByEmail(userEmail);
        AuthenticationResponse userToken = new AuthenticationResponse();
        AuthenticationResponse.UserLogin userLogin = new AuthenticationResponse.UserLogin();

        if (user != null) {
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setRole(user.getRole().getName());
            userToken.setUser(userLogin);
        }

        String token = authenticationUtils.generateToken(userToken);
        userToken.setAccessToken(token);
        userToken.setExpiryTime(new Date(
                Instant.now().plus(refreshTokenExpiration, ChronoUnit.SECONDS).toEpochMilli()));

        String refreshToken = authenticationUtils.refresh(userLogin);
        userRepository.updateRefreshToken(refreshToken, user.getEmail());
        userToken.setRefreshToken(refreshToken);

        return userToken;
    }

    public UserResponse register(UserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setRole(roleRepository.fetchRoleByName(PredefinedRole.USER_ROLE));

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserInsideTokenResponse introspect(String refresh_token) throws JOSEException, ParseException {

        SignedJWT signedJWT = authenticationUtils.verifyToken(refresh_token, false);

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        String userEmail = claimsSet.getJSONObjectClaim("user").get("email").toString();

        User user = userService.fetchUserByEmail(userEmail);

        return UserInsideTokenResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }


}
