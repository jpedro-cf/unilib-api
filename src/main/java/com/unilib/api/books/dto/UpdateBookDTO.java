package com.unilib.api.books.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record UpdateBookDTO(Optional<String> title,
                            Optional<String> description,
                            Optional<MultipartFile> image,
                            Optional<MultipartFile> pdf,
                            Optional<List<UUID>> categories) {
}
