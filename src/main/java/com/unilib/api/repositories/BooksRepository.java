package com.unilib.api.repositories;

import com.unilib.api.domain.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BooksRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b FROM Book b " +
            "WHERE (:title = '' OR b.title LIKE %:title%)")
    Page<Book> findFilteredBooks(@Param("title") String title, Pageable pageable);
}
