package com.mja123.security.domain.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserDTO(
    Long id,
    @NotNull(message = "Name is required.")
    @Length(message = "Name should be bigger than 2 character and smaller than 255", min = 2, max = 255)
    String name,
    @NotNull(message = "Lastname is required.")
    @Length(message = "Lastname should be bigger than 2 character and smaller than 255", min = 2, max = 255)
    String lastname,
    @NotNull(message = "Email is required.")
    @Length(message = "Email should be bigger than 5 character and smaller than 255", min = 5, max = 255)
    String email
) implements IUserDTO {}
