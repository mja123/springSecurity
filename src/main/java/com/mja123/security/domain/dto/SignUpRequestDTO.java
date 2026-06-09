package com.mja123.security.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
    @NotNull(message = "Email is required.")
    @Email(message = "Email must be a valid email address.")
    String email,
    @NotNull(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password,
    String firstName,
    String lastName
) {}
