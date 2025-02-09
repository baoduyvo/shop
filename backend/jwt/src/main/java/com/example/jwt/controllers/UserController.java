package com.example.jwt.controllers;

import com.example.jwt.dtos.request.user.UserCreateRequest;
import com.example.jwt.dtos.request.user.UserUpdateRequest;
import com.example.jwt.dtos.response.util.RestResponse;
import com.example.jwt.dtos.response.user.UserResponse;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<RestResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) throws EmailException {
        if (userService.existsByEmail(request.getEmail()))
            throw new EmailException("Email already exists");

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(userService.create(request))
                .message("Created User Successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<RestResponse<ResultPaginationDTO>> getAllUser(@RequestParam("page") String page,
                                                                        @RequestParam("size") String size) {

        int currentPage = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(null)
                .message("Select User Successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<UserResponse>> getUserByID(@PathVariable("id") String id) throws IDException {
        if (userService.existsByID(id))
            throw new IDException("ID User Already Exists");

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(userService.fetchUserByID(id))
                .message("Select User By ID Successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse> deleteUserByID(@PathVariable("id") String id) throws IDException {
        if (userService.existsByID(id))
            throw new IDException("ID User Not Exists");

        userService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .message("Delete User Has ID: " + id)
                .build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<RestResponse<UserResponse>> updateUserByID(@PathVariable("id") String id,
                                                                     @RequestBody UserUpdateRequest request) throws IDException {
        if (userService.existsByID(id))
            throw new IDException("ID User Not Exists");


        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .error("99999")
                .data(userService.update(id, request))
                .message("Update User Successfully")
                .build());
    }
}
