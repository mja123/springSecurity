package com.mja123.security.domain.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record BookDTO(
    Long id,
    @NotNull(message = "Title is required.")
    @Length(message = "Title should be between 2 and 255 characters", min = 2, max = 255)
    String title,
    @NotNull(message = "Author is required.")
    @Length(message = "Author should be between 2 and 255 characters", min = 2, max = 255)
    String author,
    @NotNull(message = "ISBN is required.")
    @Length(message = "ISBN should be between 10 and 20 characters", min = 10, max = 20)
    String isbn,
    String genre,
    Integer publishedYear
) implements IBookDTO {}
