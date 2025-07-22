package com.unilib.api.companies.validators.company;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.repositories.CompaniesRepository;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyExist implements Validator<UUID, Company> {
    private final CompaniesRepository companiesRepository;

    public CompanyExist(CompaniesRepository companiesRepository){
        this.companiesRepository = companiesRepository;
    }

    @Override
    public Company validate(UUID request) {
        return companiesRepository.findById(request)
                .orElseThrow(() -> new NotFoundException("Company not found."));
    }
}
