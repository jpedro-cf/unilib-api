package com.unilib.api.repositories;

import com.unilib.api.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompaniesRepository extends JpaRepository<Company, UUID> {
}
