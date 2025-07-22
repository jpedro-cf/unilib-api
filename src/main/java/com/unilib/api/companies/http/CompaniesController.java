package com.unilib.api.companies.http;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.dto.CompanyRequestDTO;
import com.unilib.api.companies.services.CompaniesService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("companies")
public class CompaniesController {
    private final CompaniesService companiesService;

    public CompaniesController(CompaniesService companiesService){
        this.companiesService = companiesService;
    }

    @PostMapping
    public ResponseEntity<Company> create(@Valid @ModelAttribute CompanyRequestDTO data,
                                          TokenAuthentication authentication) throws Exception {

        Company company = this.companiesService.create(data, authentication.getUser());

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID companyId,
                                       TokenAuthentication authentication){
        this.companiesService.delete(companyId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }
}
