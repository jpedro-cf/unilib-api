package com.unilib.api.companies.validators.dto;

import com.unilib.api.companies.CompanyRole;

import java.util.UUID;

public record AddMemberValidation(UUID companyId,
                                  UUID userId,
                                  UUID memberId,
                                  CompanyRole role) {
}
