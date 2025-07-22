package com.unilib.api.books.dto;

import com.unilib.api.books.Category;
import com.unilib.api.companies.Company;
import com.unilib.api.books.Review;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record BookResponseDTO(UUID id,
                              String title,
                              String description,
                              String image,
                              String pdf,
                              Company company,
                              List<Review> reviews,
                              List<Category> categories,
                              Instant createdAt
                              ) {
}