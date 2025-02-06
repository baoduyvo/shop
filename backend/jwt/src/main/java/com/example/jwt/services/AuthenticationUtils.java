package com.example.jwt.services;

import com.example.jwt.dtos.response.AuthenticationResponse;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationUtils {

    @NonFinal
    @Value("${jwt.base64-secret}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @NonFinal
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public String generateToken(AuthenticationResponse dto) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        AuthenticationResponse.UserInsideToken userToken = new AuthenticationResponse.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setRole(dto.getUser().getRole());

        List<String> listAuthority = new ArrayList<String>();

        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userToken.getEmail())
                .issuer("http://localhost:8080/jwt")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(accessTokenExpiration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("user", userToken)
                .claim("permission", listAuthority)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(refreshTokenExpiration, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date())))
            throw new RuntimeException("Token đã hết hạn. Vui lòng yêu cầu một token mới.");

        return signedJWT;
    }

    public String refresh(AuthenticationResponse.UserLogin request) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(request.getEmail())
                .issuer("http://localhost:8080/jwt")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(refreshTokenExpiration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("user", request)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(String role) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        stringJoiner.add("ROLE_" + role);

        return stringJoiner.toString();
    }
}
