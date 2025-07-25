package com.unilib.api.companies.validators.member;

import com.unilib.api.companies.Company;
import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.companies.validators.company.CompanyExist;
import com.unilib.api.companies.validators.dto.AddMemberValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberModificationValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.users.validators.UserExist;
import org.springframework.stereotype.Component;

@Component
public class AddCompanyMemberValidator implements Validator<AddMemberValidation, CompanyMember> {
    private final CompanyMemberExist memberExist;
    private final UserExist userExist;
    private final CompanyExist companyExist;

    public AddCompanyMemberValidator(ValidatorsFactory factory){
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
        this.userExist = factory.getValidator(UserExist.class);
        this.companyExist = factory.getValidator(CompanyExist.class);
    }

    public CompanyMember validate(AddMemberValidation request) {
        Company company = companyExist.validate(request.companyId());
        
        CompanyMember requester = memberExist.validate(new CompanyMemberValidation(
                request.companyId(), request.userId()));

        CompanyMember newMember = CompanyMember.builder()
                .role(request.role())
                .user(userExist.validate(request.memberId()))
                .company(company)
                .build();

        if(!requester.getRole().equals(CompanyRole.ADMIN)){
            throw new ForbiddenException("You're not allowed to add a member.");
        }

        return newMember;
    }

}
