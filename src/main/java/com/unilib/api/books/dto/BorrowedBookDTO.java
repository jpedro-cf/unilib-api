package com.unilib.api.books.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.shared.serializers.InstantDeserializer;
import com.unilib.api.shared.serializers.InstantSerializer;

import java.time.Instant;
import java.util.UUID;

public record BorrowedBookDTO(UUID id,
                              UUID bookId,
                              String bookTitle,
                              String username,
                              String email,
                              BorrowStatus status,
                              @JsonSerialize(using = InstantSerializer.class)
                              @JsonDeserialize(using = InstantDeserializer.class)
                              Instant releaseAt,
                              @JsonSerialize(using = InstantSerializer.class)
                              @JsonDeserialize(using = InstantDeserializer.class)
                              Instant expiresAt) {

    public static BorrowedBookDTO fromEntity(Borrow entity){
        return new BorrowedBookDTO(entity.getId(),
                entity.getBook().getId(),
                entity.getBook().getTitle(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getStatus(),
                entity.getReleaseAt(),
                entity.getExpiresAt());
    }
}
