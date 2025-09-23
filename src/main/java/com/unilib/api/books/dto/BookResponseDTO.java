package com.unilib.api.books.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unilib.api.books.Category;
import com.unilib.api.companies.Company;
import com.unilib.api.books.Review;
import com.unilib.api.shared.serializers.InstantDeserializer;
import com.unilib.api.shared.serializers.InstantSerializer;

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
                              @JsonSerialize(using = InstantSerializer.class)
                              @JsonDeserialize(using = InstantDeserializer.class)
                              Instant createdAt
                              ) {
}