package com.unilib.api.companies.validators.company;

import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.companies.validators.dto.CompanyDeletionValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class DeleteCompanyValidator implements Validator<CompanyDeletionValidation, Void> {
    private final CompanyExist companyExists;
    private final CompanyMemberExist memberExists;
    
    public DeleteCompanyValidator(ValidatorsFactory factory){
        this.companyExists = factory.getValidator(CompanyExist.class);
        this.memberExists = factory.getValidator(CompanyMemberExist.class);
    }
    
    @Override
    public Void validate(CompanyDeletionValidation request) {
        companyExists.validate(request.companyId());
        
        CompanyMember member = memberExists.validate(new CompanyMemberValidation(
                request.companyId(), request.userId()));
        
        if(!member.getRole().equals(CompanyRole.OWNER)){
            throw new ForbiddenException("Only the owner can delete a company.");
        }
        
        return null;
    }
}
