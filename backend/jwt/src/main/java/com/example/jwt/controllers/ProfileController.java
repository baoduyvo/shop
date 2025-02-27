package com.example.jwt.controllers;

import com.example.jwt.dtos.request.user.UserCreateRequest;
import com.example.jwt.dtos.request.user.UserUpdateRequest;
import com.example.jwt.dtos.response.user.UserResponse;
import com.example.jwt.dtos.response.util.RestResponse;
import com.example.jwt.dtos.response.util.ResultPaginationDTO;
import com.example.jwt.exception.error.EmailException;
import com.example.jwt.exception.error.IDException;
import com.example.jwt.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {

    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<UserResponse>> getUserByID(@PathVariable("id") String id) throws IDException {
        if (userService.existsByID(id))
            throw new IDException("ID User Already Exists");

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(userService.fetchUserByID(id))
                .message("Select Profile User By ID Successfully")
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<UserResponse>> updateUserByID(@PathVariable("id") String id,
                                                                     @RequestBody UserUpdateRequest request) throws IDException {
        if (userService.existsByID(id))
            throw new IDException("ID User Not Exists");


        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(userService.update(id, request))
                .message("Update Profile User Successfully")
                .build());
    }
}
