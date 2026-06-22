package com.mja123.security.controller.exceptions;

public record ErrorResponse(
        String type,
        String message
) {}
