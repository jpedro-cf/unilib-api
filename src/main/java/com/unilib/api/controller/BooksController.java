package com.unilib.api.controller;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.book.BookRequestDTO;
import com.unilib.api.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    @Autowired
    private BooksService booksService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Book> create(@RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam("available") String available,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("pdf") MultipartFile pdf,
                                       @RequestParam("hasEbook") String hasEbook
                                       ){
        BookRequestDTO bookRequestDTO = new BookRequestDTO(title, description, Boolean.valueOf(available), image, pdf, Boolean.valueOf(hasEbook));
        Book newBook = this.booksService.createBook(bookRequestDTO);
        return ResponseEntity.ok(newBook);
    }
}
