package com.unilib.api.books.repositories;

import com.unilib.api.books.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByUserIdAndBookId(UUID userId, UUID bookId);
    List<Review> findAllByUserId(UUID userId);
}
