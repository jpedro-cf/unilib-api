package com.unilib.api.domain.book;

import java.util.Date;
import java.util.UUID;

public record BookResponseDTO(UUID id,
                              String title,
                              Boolean available,
                              String description,
                              String image,
                              String pdf,
                              Boolean hasEbook,
                              Date createdAt,
                              Date updatedAt
                              ) {
}