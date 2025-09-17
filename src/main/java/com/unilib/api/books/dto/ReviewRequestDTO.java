package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewRequestDTO(@NotNull UUID bookId,
                               @NotNull String comment,
                               @NotNull Double rating) {
}
