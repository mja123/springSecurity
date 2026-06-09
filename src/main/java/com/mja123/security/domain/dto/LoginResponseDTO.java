package com.mja123.security.domain.dto;

public record LoginResponseDTO(
    String accessToken,
    String refreshToken,
    String tokenType,
    Integer expiresIn
) {}
