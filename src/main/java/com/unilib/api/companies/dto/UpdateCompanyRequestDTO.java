package com.unilib.api.companies.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record UpdateCompanyRequestDTO(Optional<String> name,
                                      Optional<String> description,
                                      Optional<MultipartFile> image) {
}
