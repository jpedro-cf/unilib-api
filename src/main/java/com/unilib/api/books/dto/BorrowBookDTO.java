package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record BorrowBookDTO(@NotNull Instant release,
                            @NotNull Instant expiration) {
}
