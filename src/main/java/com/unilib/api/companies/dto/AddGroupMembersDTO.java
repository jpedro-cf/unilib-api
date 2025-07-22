package com.unilib.api.companies.dto;

import java.util.List;
import java.util.UUID;

public record AddGroupMembersDTO(UUID id, List<UUID> members) {
}
