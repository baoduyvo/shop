package com.example.jwt.dtos.request.user;

import com.example.jwt.entities.Role;
import com.example.jwt.utils.constants.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {

    @NotBlank(message = "This Email is required.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please enter a valid email address."
    )
    String email;

    @NotBlank(message = "This Password is required.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and be at least 10 characters long."
    )
    String password;

    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    int age;

    GenderEnum gender;
    String address;
    String role;
}
