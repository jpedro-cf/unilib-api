package com.unilib.api.companies.validators.dto;


import java.util.UUID;

public record CompanyMemberValidation(UUID companyId, UUID userId) {
}
