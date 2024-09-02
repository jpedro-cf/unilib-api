package com.unilib.api.domain.group;

import java.util.List;
import java.util.UUID;

public record GroupRequestDTO(String name, UUID company_id, List<UUID> members) {
}
