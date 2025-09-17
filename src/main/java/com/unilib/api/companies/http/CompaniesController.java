package com.unilib.api.companies.http;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.dto.AddCompanyMemberDTO;
import com.unilib.api.companies.dto.CompanyMemberDTO;
import com.unilib.api.companies.dto.CompanyRequestDTO;
import com.unilib.api.companies.dto.UpdateCompanyRequestDTO;
import com.unilib.api.companies.services.CompaniesService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable("id") UUID companyId,
                                          @Valid @ModelAttribute UpdateCompanyRequestDTO data,
                                          TokenAuthentication authentication) throws Exception {

        Company company = this.companiesService.update(companyId, data, authentication.getUser());

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID companyId,
                                       TokenAuthentication authentication){
        this.companiesService.delete(companyId, authentication.getUser());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") UUID companyId,
                                       TokenAuthentication authentication){
        Company company = this.companiesService.getByID(companyId);

        return ResponseEntity.ok(company);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<CompanyMemberDTO>> getMembers(@PathVariable("id") UUID companyId,
                                                             TokenAuthentication authentication){
        List<CompanyMemberDTO> response = this.companiesService
                .getMembers(companyId, authentication.getUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<CompanyMember> addMember(@PathVariable("id") UUID companyId,
                                                   @RequestBody @Valid AddCompanyMemberDTO request,
                                                   TokenAuthentication authentication){
        CompanyMember member = this.companiesService
                .addMember(companyId,authentication.getUser(),request);

        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{companyId}/members/{memberId}")
    public ResponseEntity<Void> addMember(@PathVariable("companyId") UUID companyId,
                                                   @PathVariable("memberId") UUID memberId,
                                                   TokenAuthentication authentication){

        this.companiesService.removeMember(companyId,authentication.getUser(), memberId);

        return ResponseEntity.noContent().build();
    }
}
