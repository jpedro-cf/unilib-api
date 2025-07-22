package com.unilib.api.companies.repositories;

import com.unilib.api.companies.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupsRepository extends JpaRepository<Group, UUID> {
    List<Group> findByCompanyId(UUID companyId);
}
