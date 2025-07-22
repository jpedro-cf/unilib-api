package com.unilib.api.companies.repositories;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompaniesRepository extends JpaRepository<Company, UUID> {
}
