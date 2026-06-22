package com.mja123.security.controller;

import com.mja123.security.domain.dto.BookDTO;
import com.mja123.security.domain.dto.UpdateBookDTO;
import com.mja123.security.domain.service.BookService;
import com.mja123.security.exceptions.NotFoundException;
import com.mja123.security.exceptions.NotUniqueAttributeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_read:books', 'ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();

        if (books.isEmpty())
            return ResponseEntity.ok(books);
        return ResponseEntity.accepted().body(books);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_read:books', 'ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<BookDTO> getBook(@PathVariable long id) {
        try {
            return ResponseEntity.ok(bookService.getBook(id));
        } catch (NotFoundException error) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_write:books', 'ROLE_ADMIN')")
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookDTO bookDTO) throws NotUniqueAttributeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_write:books', 'ROLE_ADMIN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable long id, @RequestBody @Valid UpdateBookDTO bookData)
            throws NotFoundException {
        return ResponseEntity.ok(bookService.updateBook(id, bookData));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_delete:books', 'ROLE_ADMIN')")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable long id) throws NotFoundException {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }
}
