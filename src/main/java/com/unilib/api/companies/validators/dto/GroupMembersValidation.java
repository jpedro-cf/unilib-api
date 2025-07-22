package com.unilib.api.companies.validators.dto;

import java.util.List;
import java.util.UUID;

public record GroupMembersValidation(UUID groupId,
                                     UUID userId,
                                     List<UUID> memberIds) {
}
