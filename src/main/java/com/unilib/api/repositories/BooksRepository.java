package com.unilib.api.repositories;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BooksRepository extends JpaRepository<Book, UUID> {
    // Vai ter filtro por categoria futuramente
    @Query("SELECT b FROM Book b " +
            "WHERE (:title = '' OR b.title LIKE %:title%)")
    Page<Book> findFilteredBooks(@Param("title") String title, Pageable pageable);

    List<Book> findAllByCompany(Company company);
}
