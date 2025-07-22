package com.unilib.api.companies.validators.dto;

import com.unilib.api.users.User;

import java.util.UUID;

public record CompanyDeletionValidation(UUID companyId, UUID userId) {
}
