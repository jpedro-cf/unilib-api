package com.unilib.api.companies.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record CompanyRequestDTO(String name,
                                String description,
                                Optional<MultipartFile> image) {
}
