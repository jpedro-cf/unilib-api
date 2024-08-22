package com.unilib.api.domain.book;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record BookRequestDTO(String title,
                             String description,
                             Boolean available,
                             MultipartFile image,
                             MultipartFile pdf,
                             Boolean hasEbook,
                             Optional<List<UUID>> categories
                             ){}
