package com.unilib.api.companies.dto;

import java.util.UUID;

public record CompanyResponseDTO(UUID id, String name, String image) {
}
