package com.unilib.api.repositories;

import com.unilib.api.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BooksRepository extends JpaRepository<Book, UUID> {

}
