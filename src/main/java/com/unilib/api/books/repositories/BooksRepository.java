package com.unilib.api.books.repositories;

import com.unilib.api.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Book, UUID>, PagingAndSortingRepository<Book, UUID> {
    List<Book> findAllByCompanyId(UUID companyId);
}
