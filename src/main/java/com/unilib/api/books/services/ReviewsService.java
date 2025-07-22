package com.unilib.api.books.services;

import com.unilib.api.books.Book;
import com.unilib.api.books.Review;
import com.unilib.api.books.dto.ReviewRequestDTO;
import com.unilib.api.books.validators.dto.ReviewValidation;
import com.unilib.api.books.validators.reviews.ReviewValidator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import com.unilib.api.books.repositories.ReviewsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ValidatorsFactory validatorsFactory;

    public ReviewsService(ReviewsRepository reviewsRepository,
                          ValidatorsFactory validatorsFactory){
        this.reviewsRepository = reviewsRepository;
        this.validatorsFactory = validatorsFactory;
    }

    public Review addBookReview(UUID bookId,ReviewRequestDTO request, User user){
        ReviewValidator validator = validatorsFactory.getValidator(ReviewValidator.class);
        Book book = validator.validate(new ReviewValidation(bookId, user.getId(), request));

        Review review = Review.builder()
                .user(user)
                .book(book)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(Instant.now())
                .build();

        return this.reviewsRepository.save(review);
    }

    public void deleteReview(UUID reviewId, User user){

        Review review = this.reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found."));

        if(!review.getUser().getId().equals(user.getId())){
            throw new ForbiddenException("Only the user can delete his review.");
        }

        this.reviewsRepository.deleteById(reviewId);
    }
}
