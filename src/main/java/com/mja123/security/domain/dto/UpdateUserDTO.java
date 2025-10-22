package com.mja123.security.domain.dto;

public record UpdateUserDTO (
    String name,
    String lastname,
    String email
) implements IUserDTO {}
