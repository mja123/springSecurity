package com.mja123.security.domain.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequestDTO(
    @NotNull(message = "Refresh token is required.")
    String refreshToken
) {}
