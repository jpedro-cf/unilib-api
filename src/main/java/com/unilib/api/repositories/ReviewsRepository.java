package com.unilib.api.repositories;

import com.unilib.api.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewsRepository extends JpaRepository<Review, UUID> {
}
