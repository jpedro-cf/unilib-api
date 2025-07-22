package com.unilib.api.companies.services;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.dto.AddCompanyMemberDTO;
import com.unilib.api.companies.dto.CompanyRequestDTO;
import com.unilib.api.companies.dto.CompanyResponseDTO;
import com.unilib.api.companies.repositories.CompanyMembersRepository;
import com.unilib.api.companies.validators.member.AddCompanyMemberValidator;
import com.unilib.api.companies.validators.member.RemoveCompanyMemberValidator;
import com.unilib.api.companies.validators.dto.AddMemberValidation;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.companies.validators.company.DeleteCompanyValidator;
import com.unilib.api.companies.validators.dto.RemoveMemberValidation;
import com.unilib.api.companies.validators.dto.CompanyDeletionValidation;
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
        CompanyMember member = CompanyMember.builder()
                .role(CompanyRole.OWNER)
                .user(user)
                .build();

        Company company = Company.builder()
                .name(request.name())
                .description(request.description())
                .members(new HashSet<>(Set.of(member)))
                .build();

        if(request.image().isPresent()){
            company.setImage("images/company/" + UUID.randomUUID());

            storage.uploadObject(company.getImage(),
                    request.image().get().getBytes(),
                    Map.of());
        }

        member.setCompany(company);

        companiesRepository.save(company);

        return company;
    }

    public void delete(UUID id, User user){
        DeleteCompanyValidator validator = validatorsFactory
                .getValidator(DeleteCompanyValidator.class);

        validator.validate(new CompanyDeletionValidation(id, user.getId()));

        this.companiesRepository.deleteById(id);
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

        validator.validate(new RemoveMemberValidation(
                companyId,
                memberId,
                user.getId()
        ));

        companyMembersRepository.deleteById(memberId);
    }

}
