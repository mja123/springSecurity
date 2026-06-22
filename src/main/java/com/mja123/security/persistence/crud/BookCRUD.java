package com.mja123.security.persistence.crud;

import com.mja123.security.persistence.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookCRUD extends CrudRepository<BookEntity, Long> {
    BookEntity findBookByIsbn(String isbn);
}
