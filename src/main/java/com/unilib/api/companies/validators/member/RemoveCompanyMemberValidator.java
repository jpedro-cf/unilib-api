package com.unilib.api.companies.validators.member;

import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.validators.company.CompanyExist;
import com.unilib.api.companies.validators.dto.CompanyMemberModificationValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.dto.RemoveMemberValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class RemoveCompanyMemberValidator implements Validator<RemoveMemberValidation, Void> {
    private final CompanyExist companyExist;
    private final CompanyMemberExist memberExist;

    public RemoveCompanyMemberValidator(ValidatorsFactory factory){
        this.companyExist = factory.getValidator(CompanyExist.class);
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
    }

    public Void validate(RemoveMemberValidation request) {
        companyExist.validate(request.companyId());

        CompanyMember requester = memberExist.validate(new CompanyMemberValidation(
                request.companyId(), request.requesterId()));

        CompanyMember member = memberExist.validate(new CompanyMemberValidation(
                request.companyId(), request.memberId()));

        if(requester.getRole().getLevel() < member.getRole().getLevel()){
            throw new ForbiddenException("You're not allowed to remove this member.");
        }

        return null;
    }

}
