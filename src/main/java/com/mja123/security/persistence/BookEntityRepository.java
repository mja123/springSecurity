package com.mja123.security.persistence;

import com.mja123.security.domain.repository.BookRepository;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import com.mja123.security.persistence.crud.BookCRUD;
import com.mja123.security.persistence.entity.BookEntity;
import com.mja123.security.utils.ParsingUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookEntityRepository implements BookRepository {
    private final BookCRUD bookCRUD;

    public BookEntityRepository(BookCRUD bookCRUD) {
        this.bookCRUD = bookCRUD;
    }

    @Override
    public List<BookEntity> getAll() {
        return (List<BookEntity>) bookCRUD.findAll();
    }

    @Override
    public Optional<BookEntity> getById(long id) {
        return bookCRUD.findById(id);
    }

    @Override
    public BookEntity add(BookEntity book) throws NotUniqueAttributeException {
        if (bookCRUD.findBookByIsbn(book.getIsbn()) != null) {
            throw new NotUniqueAttributeException("ISBN is already registered!");
        }
        return bookCRUD.save(book);
    }

    @Override
    public Optional<BookEntity> update(long id, BookEntity updatedBook) {
        BookEntity bookEntity = bookCRUD.findById(id).orElse(null);

        if (bookEntity != null) {
            ParsingUtil.setAttributesFromEntityToEntity(bookEntity, updatedBook);
            bookCRUD.save(bookEntity);
        }
        return Optional.ofNullable(bookEntity);
    }

    @Override
    public Optional<BookEntity> delete(long id) {
        BookEntity book = bookCRUD.findById(id).orElse(null);
        if (book != null) {
            bookCRUD.delete(book);
        }
        return Optional.ofNullable(book);
    }
}
