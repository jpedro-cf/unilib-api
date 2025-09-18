package com.unilib.api.books.validators.dto;

import com.unilib.api.books.dto.UpdateBookDTO;
import com.unilib.api.users.User;

import java.util.UUID;

public record UpdateBookValidation(UUID bookId, UpdateBookDTO data, User user) {
}
