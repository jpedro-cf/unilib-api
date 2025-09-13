package com.unilib.api.books.dto;

import com.unilib.api.users.User;

import java.util.Optional;
import java.util.UUID;

public record GetBorrowsDTO(Optional<UUID> companyId, User user) {
}
