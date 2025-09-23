package com.unilib.api.companies.validators.groups;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.repositories.CompaniesRepository;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.dto.GroupValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CreateGroupValidator implements Validator<GroupValidation, Company> {
    private final CompaniesRepository companiesRepository;
    private final CompanyMemberExist memberExist;

    public CreateGroupValidator(CompaniesRepository companiesRepository,
                                ValidatorsFactory factory){
        this.companiesRepository = companiesRepository;
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
    }

    @Override
    public Company validate(GroupValidation request) {
        CompanyMember member = memberExist
                .validate(new CompanyMemberValidation(request.companyId(), request.userId()));

        if(member.getRole().getLevel() < CompanyRole.ADMIN.getLevel()){
            throw new ForbiddenException("Você não tem permissão para adicionar uam turma.");
        }

        return companiesRepository.findById(request.companyId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada."));
    }
}
