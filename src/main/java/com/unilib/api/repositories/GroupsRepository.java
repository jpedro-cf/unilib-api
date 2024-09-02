package com.unilib.api.repositories;

import com.unilib.api.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupsRepository extends JpaRepository<Group, UUID> {
    List<Group> findByCompanyId(UUID companyId);
}
