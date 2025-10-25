package com.mja123.security.domain.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateUserDTO (
    @Length(message = "Name should be bigger than 2 character and smaller than 255", min = 2, max = 255)
    String name,
    @Length(message = "Lastname should be bigger than 2 character and smaller than 255", min = 2, max = 255)
    String lastname,
    @Length(message = "Email should be bigger than 5 character and smaller than 255", min = 5, max = 255)
    String email
) implements IUserDTO {}
