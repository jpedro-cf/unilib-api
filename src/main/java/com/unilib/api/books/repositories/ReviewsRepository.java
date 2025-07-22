package com.unilib.api.books.repositories;

import com.unilib.api.books.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, UUID> {
}
