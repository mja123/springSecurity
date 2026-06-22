package com.mja123.security.domain.service;

import com.mja123.security.domain.dto.BookDTO;
import com.mja123.security.domain.dto.UpdateBookDTO;
import com.mja123.security.domain.repository.BookRepository;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import com.mja123.security.persistence.entity.BookEntity;
import com.mja123.security.persistence.mapper.BookMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getAllBooks() {
        return bookMapper.entitiesToBooks(bookRepository.getAll());
    }

    public BookDTO getBook(long id) throws NotFoundException {
        BookEntity bookEntity = bookRepository.getById(id).orElse(null);
        if (bookEntity == null) throw new NotFoundException("Book with id " + id + " not found");
        return bookMapper.entityToBook(bookEntity);
    }

    public BookDTO addBook(BookDTO bookDTO) throws NotUniqueAttributeException {
        return bookMapper.entityToBook(bookRepository.add(bookMapper.bookToEntity(bookDTO)));
    }

    public BookDTO updateBook(long id, UpdateBookDTO bookDTO) throws NotFoundException {
        BookEntity bookEntity = bookMapper.updateBookToEntity(bookDTO);
        BookEntity updatedBook = bookRepository.update(id, bookEntity).orElse(null);
        if (updatedBook == null) throw new NotFoundException("Book with id " + id + " not found");
        return bookMapper.entityToBook(updatedBook);
    }

    public BookDTO deleteBook(long id) throws NotFoundException {
        BookEntity bookEntity = bookRepository.delete(id).orElse(null);
        if (bookEntity == null) throw new NotFoundException("Book with id " + id + " not found");
        return bookMapper.entityToBook(bookEntity);
    }
}
