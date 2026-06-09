package com.mja123.security.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
    @NotNull(message = "Email is required.")
    @Email(message = "Email must be a valid email address.")
    String email,
    @NotNull(message = "Password is required.")
    String password
) {}
