package com.unilib.api.books.validators.dto;

import java.util.UUID;

public record BookBorrowValidation(UUID bookId, UUID userId) {
}
