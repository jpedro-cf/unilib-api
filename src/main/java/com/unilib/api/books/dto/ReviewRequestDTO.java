package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(@NotNull String comment,
                               @NotNull Double rating) {
}
