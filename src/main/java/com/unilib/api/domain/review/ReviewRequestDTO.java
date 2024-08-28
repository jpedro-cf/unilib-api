package com.unilib.api.domain.review;

import java.util.UUID;

public record ReviewRequestDTO(UUID book_id, Double rating) {
}
