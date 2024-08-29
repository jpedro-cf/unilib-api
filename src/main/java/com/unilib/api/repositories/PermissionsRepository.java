package com.unilib.api.repositories;

import com.unilib.api.domain.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionsRepository extends JpaRepository<Permission, UUID> {
}
