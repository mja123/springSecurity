package com.mja123.security.domain.repository;

import com.mja123.security.exceptions.NotUniqueAttributeException;
import com.mja123.security.persistence.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<BookEntity> getAll();
    Optional<BookEntity> getById(long id);
    BookEntity add(BookEntity book) throws NotUniqueAttributeException;
    Optional<BookEntity> update(long id, BookEntity book);
    Optional<BookEntity> delete(long id);
}
