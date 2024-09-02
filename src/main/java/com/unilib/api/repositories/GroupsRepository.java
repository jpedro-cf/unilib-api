package com.unilib.api.repositories;

import com.unilib.api.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupsRepository extends JpaRepository<Group, UUID> {
}
