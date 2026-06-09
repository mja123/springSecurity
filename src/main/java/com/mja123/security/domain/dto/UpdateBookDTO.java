package com.mja123.security.domain.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateBookDTO(
    @Length(message = "Title should be between 2 and 255 characters", min = 2, max = 255)
    String title,
    @Length(message = "Author should be between 2 and 255 characters", min = 2, max = 255)
    String author,
    @Length(message = "ISBN should be between 10 and 20 characters", min = 10, max = 20)
    String isbn,
    String genre,
    Integer publishedYear
) implements IBookDTO {}
