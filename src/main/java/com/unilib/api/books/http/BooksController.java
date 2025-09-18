package com.unilib.api.books.http;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.dto.*;
import com.unilib.api.books.services.BooksService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("books")
public class BooksController {
    private final BooksService booksService;

    public BooksController(BooksService booksService){
        this.booksService = booksService;
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @ModelAttribute AddBookRequestDTO data,
                                       TokenAuthentication authentication) throws Exception {

        Book newBook = this.booksService.addBook(data, authentication.getUser());

        return ResponseEntity.ok(newBook);
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> update(@PathVariable("id") UUID id,
                                       @Valid @ModelAttribute UpdateBookDTO data,
                                       TokenAuthentication authentication) throws Exception {

        Book book = this.booksService.updateBook(id, data, authentication.getUser());

        return ResponseEntity.ok(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable UUID id) {
        Book book = this.booksService.getByID(id);

        return ResponseEntity.ok(book);
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<ReadBookResponse> readBook(@PathVariable("id") UUID id,
                                                     TokenAuthentication authentication) {
        ReadBookResponse data = this.booksService.readBook(id, authentication.getUser());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<Borrow> borrowBook(@PathVariable("id") UUID id,
                                             @RequestBody @Valid BorrowBookDTO body,
                                             TokenAuthentication authentication) {
        Borrow data = this.booksService.borrowBook(id, body, authentication.getUser());

        return ResponseEntity.ok(data);
    }

    @PutMapping("/borrows/{id}")
    public ResponseEntity<Void> acceptBorrow(@PathVariable("id") UUID borrowId,
                                             TokenAuthentication authentication) {
        this.booksService.acceptBorrow(borrowId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/borrows/{id}")
    public ResponseEntity<Void> denyBorrow(@PathVariable("id") UUID borrowId,
                                             TokenAuthentication authentication) {
        this.booksService.denyBorrow(borrowId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(@RequestParam(required = false) UUID companyId,
                                             Pageable pageable) {
        if(companyId != null){
            return ResponseEntity.ok(this.booksService.getBooksByCompany(companyId, pageable));
        }

        return ResponseEntity.ok(this.booksService.getBooks(pageable));
    }

    @GetMapping("/borrows")
    public ResponseEntity<Page<BorrowedBookDTO>> getBorrows(@RequestParam(required = false) UUID companyId,
                                                            Pageable pageable,
                                                   TokenAuthentication authentication) {
        GetBorrowsDTO data = new GetBorrowsDTO(Optional.ofNullable(companyId),
                authentication.getUser(),
                pageable);

        Page<BorrowedBookDTO> response = this.booksService.getBorrows(data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") UUID bookId,
                                           TokenAuthentication authentication) {
        this.booksService.deleteBook(bookId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }
}
