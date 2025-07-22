package com.unilib.api.books.validators.dto;

import java.util.UUID;

public record AcceptBorrowValidation(UUID borrowId, UUID userId) {
}
