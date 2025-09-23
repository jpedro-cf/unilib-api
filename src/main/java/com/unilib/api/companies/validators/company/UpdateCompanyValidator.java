package com.unilib.api.companies.validators.company;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.dto.CompanyUpdateValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class UpdateCompanyValidator implements Validator<CompanyUpdateValidation, Company> {
    private final CompanyExist companyExists;
    private final CompanyMemberExist memberExists;

    public UpdateCompanyValidator(ValidatorsFactory factory){
        this.companyExists = factory.getValidator(CompanyExist.class);
        this.memberExists = factory.getValidator(CompanyMemberExist.class);
    }
    @Override
    public Company validate(CompanyUpdateValidation request) {
        Company company = companyExists.validate(request.companyId());

        CompanyMember member = memberExists.validate(new CompanyMemberValidation(
                request.companyId(), request.userId()));

        if(!member.getRole().equals(CompanyRole.OWNER)){
            throw new ForbiddenException("Apenas o dono pode atualizar a empresa.");
        }
        return company;
    }
}
