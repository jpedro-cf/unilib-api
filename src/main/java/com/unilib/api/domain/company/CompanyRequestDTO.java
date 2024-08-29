package com.unilib.api.domain.company;

import org.springframework.web.multipart.MultipartFile;

public record CompanyRequestDTO(String name, String description, MultipartFile image) {
}
