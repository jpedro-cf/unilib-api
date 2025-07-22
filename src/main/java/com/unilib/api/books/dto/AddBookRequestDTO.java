package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record AddBookRequestDTO(@NotNull String title,
                                @NotNull String description,
                                @NotNull MultipartFile image,
                                @NotNull MultipartFile pdf,
                                @NotNull UUID companyId,
                                Optional<List<UUID>> categories){}
