package com.unilib.api.companies.dto;

import com.unilib.api.companies.CompanyRole;

import java.util.UUID;

public record CompanyMemberDTO(UUID id,
                               UUID companyId,
                               String name,
                               String email,
                               CompanyRole role) {
}
