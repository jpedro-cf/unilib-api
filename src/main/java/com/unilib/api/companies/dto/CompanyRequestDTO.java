package com.unilib.api.companies.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CompanyRequestDTO(@NotNull String name,
                                @NotNull String description,
                                @NotNull MultipartFile image) {
}
