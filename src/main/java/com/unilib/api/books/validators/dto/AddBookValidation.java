package com.unilib.api.books.validators.dto;

import com.unilib.api.books.dto.AddBookRequestDTO;
import com.unilib.api.users.User;

public record AddBookValidation(AddBookRequestDTO data, User user) {
}
