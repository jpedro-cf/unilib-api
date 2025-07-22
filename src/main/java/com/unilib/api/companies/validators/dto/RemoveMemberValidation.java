package com.unilib.api.companies.validators.dto;


import java.util.UUID;

public record RemoveMemberValidation(UUID companyId,
                                     UUID memberId,
                                     UUID requesterId) {}
