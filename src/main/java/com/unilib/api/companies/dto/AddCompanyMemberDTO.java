package com.unilib.api.companies.dto;

import com.unilib.api.companies.CompanyRole;

import java.util.UUID;

public record AddCompanyMemberDTO(UUID memberId, CompanyRole role) {
}
