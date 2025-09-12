package com.unilib.api.books.http;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.dto.AddBookRequestDTO;
import com.unilib.api.books.dto.BookResponseDTO;
import com.unilib.api.books.dto.BorrowBookDTO;
import com.unilib.api.books.dto.ReadBookResponse;
import com.unilib.api.books.services.BooksService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<BookResponseDTO>> getFiltered(@RequestParam(defaultValue = "0", required = false) int page,
                                                             @RequestParam(defaultValue = "10", required = false) int size,
                                                             @RequestParam(defaultValue = "DESC", required = false) String sort
                                                             ) {
       List<BookResponseDTO> books = this.booksService.getBooks(sort, page, size);
       return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") UUID bookId,
                                           TokenAuthentication authentication) {
        this.booksService.deleteBook(bookId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }
}
