package com.unilib.api.books.dto;

import com.unilib.api.users.User;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public record GetBorrowsDTO(Optional<UUID> companyId, User user, Pageable pageable) {
}
