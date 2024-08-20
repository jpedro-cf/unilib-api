package com.unilib.api.service;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.book.BookRequestDTO;
import com.unilib.api.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class BooksService {
    @Autowired
    private BooksRepository repository;

    public Book createBook(BookRequestDTO data) {
        String imgUrl = null;
        String pdf = null;

        if(data.image() != null){
            imgUrl = this.uploadFile(data.image());
        }

        if(data.pdf() != null){
            pdf = this.uploadFile(data.pdf());
        }

        Book newBook = new Book();
        newBook.setTitle(data.title());
        newBook.setDescription(data.description());
        newBook.setAvailable(data.available());
        newBook.setImage(imgUrl);
        newBook.setPdf(pdf);
        newBook.setHasEbook(data.hasEbook());
        newBook.setCreatedAt(new Date());
        newBook.setUpdatedAt(new Date());

        repository.save(newBook);


        return newBook;
    }

    private String uploadFile(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();

        if (contentType != null && contentType.startsWith("image/")) {
            return "https://example.com/uploaded/image";
        }

        if ("application/pdf".equals(contentType)) {
            return "https://example.com/uploaded/pdf";
        }

        // Tipo de arquivo não suportado
        throw new IllegalArgumentException("Apenas imagens e PDFs são suportados.");
    }
}
