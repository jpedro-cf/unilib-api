package com.unilib.api.domain.group;

import java.util.List;
import java.util.UUID;

public record AddGroupMembersDTO(UUID id, List<UUID> members) {
}
