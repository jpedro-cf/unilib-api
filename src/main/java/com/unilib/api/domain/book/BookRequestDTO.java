package com.unilib.api.domain.book;

import org.springframework.web.multipart.MultipartFile;

public record BookRequestDTO(String title,
                             String description,
                             Boolean available,
                             MultipartFile image,
                             MultipartFile pdf,
                             Boolean hasEbook
                             ){}
