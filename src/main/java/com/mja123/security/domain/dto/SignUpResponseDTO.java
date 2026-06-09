package com.mja123.security.domain.dto;

public record SignUpResponseDTO(
    String email,
    boolean emailVerified
) {}
