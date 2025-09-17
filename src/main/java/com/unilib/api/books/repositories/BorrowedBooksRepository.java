package com.unilib.api.books.repositories;

import com.unilib.api.books.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowedBooksRepository extends JpaRepository<Borrow, UUID>, PagingAndSortingRepository<Borrow, UUID> {
    List<Borrow> getByBookIdAndUserId(UUID bookId, UUID userId);
    Page<Borrow> getByUserId(UUID userId, Pageable pageable);
    Page<Borrow> findAllByCompanyId(UUID companyId, Pageable pageable);
}
