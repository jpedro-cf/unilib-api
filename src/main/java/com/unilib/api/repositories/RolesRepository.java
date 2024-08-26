package com.unilib.api.repositories;

import com.unilib.api.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolesRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
