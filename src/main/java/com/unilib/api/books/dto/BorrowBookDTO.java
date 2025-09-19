package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Optional;

public record BorrowBookDTO(Optional<Instant> release,
                            @NotNull Instant expiration) {
}
