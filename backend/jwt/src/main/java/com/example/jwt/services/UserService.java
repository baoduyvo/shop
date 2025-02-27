package com.example.jwt.services;

import com.example.jwt.dtos.request.user.UserCreateRequest;
import com.example.jwt.dtos.request.user.UserUpdateRequest;
import com.example.jwt.dtos.response.user.UserResponse;
import com.example.jwt.dtos.response.util.Pagination;
import com.example.jwt.dtos.response.util.ResultPaginationDTO;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.example.jwt.repositories.RoleRepository;
import com.example.jwt.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public ResultPaginationDTO fillAllUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setResult(userPage.getContent().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .age(user.getAge())
                        .gender(user.getGender())
                        .address(user.getAddress())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList()));

        resultPaginationDTO.setPagination(Pagination.builder()
                .totalPages(userPage.getTotalPages())
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalData(userPage.getTotalElements())
                .build());

        return resultPaginationDTO;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByID(String id) {
        return userRepository.findById(id).isPresent() ? false : true;
    }

    public boolean delete(String id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Delete User Failed", e);
        }
    }

    public UserResponse fetchUserByID(String id) {
        User user = userRepository.findById(id).orElse(null);
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

    public User fetchUserByEmail(String email) {
        return userRepository.fetchUserByEmail(email);
    }

    public UserResponse create(UserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setRole(roleRepository.findById(Long.valueOf(request.getRole())).orElse(null));
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

    public UserResponse update(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElse(null);

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setPassword(user.getPassword());
        }

        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());

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
}
