package com.unilib.api.books.validators.dto;

import com.unilib.api.books.dto.ReviewRequestDTO;

import java.util.UUID;

public record ReviewValidation(UUID bookId, UUID userId, ReviewRequestDTO data) {
}
