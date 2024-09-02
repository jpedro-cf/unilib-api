package com.unilib.api.repositories;

import com.unilib.api.domain.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionsRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByCompanyIdAndUserId(UUID companyId, UUID userId);

}
