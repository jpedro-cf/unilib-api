package com.unilib.api.controller;

import com.unilib.api.domain.review.Review;
import com.unilib.api.domain.review.ReviewRequestDTO;
import com.unilib.api.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("reviews")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;

    @PostMapping()
    public ResponseEntity<Review> create(@RequestBody ReviewRequestDTO request){
        var review = this.reviewsService.create(request);

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.reviewsService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
