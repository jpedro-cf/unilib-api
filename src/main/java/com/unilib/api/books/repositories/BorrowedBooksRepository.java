package com.unilib.api.books.repositories;

import com.unilib.api.books.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowedBooksRepository extends JpaRepository<Borrow, UUID> {
    List<Borrow> getByBookIdAndUserId(UUID bookId, UUID userId);
    List<Borrow> getByUserId(UUID userId);
    List<Borrow> findAllByCompanyId(UUID companyId);
}
