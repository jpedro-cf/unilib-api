package com.unilib.api.companies.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record CompanyRequestDTO(@NotNull String name,
                                @NotNull String description,
                                Optional<MultipartFile> image) {
}
