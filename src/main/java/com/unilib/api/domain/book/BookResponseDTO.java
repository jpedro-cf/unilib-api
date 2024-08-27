package com.unilib.api.domain.book;

import com.unilib.api.domain.category.Category;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record BookResponseDTO(UUID id,
                              String title,
                              String description,
                              String image,
                              String pdf,
                              Set<Category> categories,
                              Date createdAt
                              ) {
}