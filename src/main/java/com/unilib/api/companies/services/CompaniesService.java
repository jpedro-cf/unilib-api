package com.unilib.api.companies.services;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.dto.*;
import com.unilib.api.companies.repositories.CompanyMembersRepository;
import com.unilib.api.companies.validators.company.UpdateCompanyValidator;
import com.unilib.api.companies.validators.dto.*;
import com.unilib.api.companies.validators.member.AddCompanyMemberValidator;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.companies.validators.member.RemoveCompanyMemberValidator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.companies.validators.company.DeleteCompanyValidator;
import com.unilib.api.shared.Storage;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import com.unilib.api.companies.repositories.CompaniesRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompaniesService {
    private final CompaniesRepository companiesRepository;
    private final CompanyMembersRepository companyMembersRepository;

    private final Storage storage;
    private final ValidatorsFactory validatorsFactory;

    public CompaniesService(CompaniesRepository companiesRepository,
                            CompanyMembersRepository companyMembersRepository,
                            Storage storage,
                            ValidatorsFactory validatorsFactory){
        this.companiesRepository = companiesRepository;
        this.companyMembersRepository = companyMembersRepository;
        this.storage = storage;
        this.validatorsFactory = validatorsFactory;
    }

    public Company getByID(UUID id){
        return this.companiesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));
    }

    public List<CompanyResponseDTO> getUserCompanies(User user){
        return user.getMemberships().stream()
                .map(m -> {
                    Company company = m.getCompany();
                    return new CompanyResponseDTO(company.getId(),
                            company.getName(),
                            company.getImage());
                }).toList();
    }


    public Company create(CompanyRequestDTO request, User user) throws Exception {
        Company company = Company.builder()
                .name(request.name())
                .description(request.description())
                .members(new HashSet<>())
                .build();

        CompanyMember member = CompanyMember.builder()
                .role(CompanyRole.OWNER)
                .user(user)
                .company(company)
                .build();

        company.getMembers().add(member);

        company.setImage("images/company/" + UUID.randomUUID());
        storage.uploadObject(company.getImage(),
                request.image().getBytes(),
                Map.of());

        return companiesRepository.save(company);
    }

    public Company update(UUID companyId, UpdateCompanyRequestDTO request, User user) throws Exception {
        UpdateCompanyValidator validator = validatorsFactory
                .getValidator(UpdateCompanyValidator.class);

        Company company = validator.validate(new CompanyUpdateValidation(companyId, user.getId()));

        company.setName(request.name().orElseGet(company::getName));
        company.setDescription(request.description().orElseGet(company::getDescription));

        if(request.image().isPresent()){
            company.setImage("images/company/" + UUID.randomUUID());

            storage.uploadObject(company.getImage(),
                    request.image().get().getBytes(),
                    Map.of());
        }

        companiesRepository.save(company);

        return company;
    }

    public void delete(UUID id, User user){
        DeleteCompanyValidator validator = validatorsFactory
                .getValidator(DeleteCompanyValidator.class);

        validator.validate(new CompanyDeletionValidation(id, user.getId()));

        this.companiesRepository.deleteById(id);
    }

    public List<CompanyMemberDTO> getMembers(UUID companyId, User user){
        CompanyMemberExist validator = validatorsFactory
                .getValidator(CompanyMemberExist.class);

        validator.validate(new CompanyMemberValidation(companyId, user.getId()));

        return this.companyMembersRepository.findAllByCompanyId(companyId)
                .stream()
                .map(m -> new CompanyMemberDTO(m.getUser().getId(),
                        m.getCompany().getId(),
                        m.getUser().getName(),
                        m.getUser().getEmail(), m.getRole())
        ).toList();
    }

    public CompanyMember addMember(UUID companyId,
                                   User user,
                                   AddCompanyMemberDTO request){

        AddCompanyMemberValidator validator = validatorsFactory
                .getValidator(AddCompanyMemberValidator.class);

        CompanyMember newMember = validator.validate(new AddMemberValidation(companyId,
                user.getId(), request.memberId(), request.role()));

        companyMembersRepository.save(newMember);

        return newMember;
    }

    public void removeMember(UUID companyId,
                             User user,
                             UUID memberId){
        RemoveCompanyMemberValidator validator = validatorsFactory
                .getValidator(RemoveCompanyMemberValidator.class);

        CompanyMember member = validator.validate(new RemoveMemberValidation(
                companyId,
                memberId,
                user.getId()
        ));

        companyMembersRepository.delete(member);
    }

}
