package com.unilib.api.books.http;

import com.unilib.api.books.Review;
import com.unilib.api.books.dto.ReviewRequestDTO;
import com.unilib.api.books.services.ReviewsService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("reviews")
public class ReviewsController {
    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService){
        this.reviewsService = reviewsService;
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody @Valid ReviewRequestDTO request,
                                         TokenAuthentication authentication){
        var review = this.reviewsService
                .addBookReview(request, authentication.getUser());

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       TokenAuthentication authentication){
        this.reviewsService.deleteReview(id, authentication.getUser());

        return ResponseEntity.noContent().build();
    }
}
