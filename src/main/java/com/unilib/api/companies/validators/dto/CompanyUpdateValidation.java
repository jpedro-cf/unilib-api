package com.unilib.api.companies.validators.dto;

import com.unilib.api.companies.CompanyMember;

import java.util.UUID;

public record CompanyUpdateValidation(UUID companyId, UUID userId) {
}
