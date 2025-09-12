package com.unilib.api.companies.validators.member;

import com.unilib.api.companies.CompanyMember;
import com.unilib.api.companies.repositories.CompanyMembersRepository;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.ForbiddenException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CompanyMemberExist implements Validator<CompanyMemberValidation, CompanyMember> {
    private final CompanyMembersRepository companyMembersRepository;

    public CompanyMemberExist(CompanyMembersRepository companyMembersRepository){
        this.companyMembersRepository = companyMembersRepository;
    }

    @Override
    public CompanyMember validate(CompanyMemberValidation request) {
        Optional<CompanyMember> member = companyMembersRepository
                .findByCompanyIdAndUserId(request.companyId(), request.userId());

        if(member.isEmpty()){
            throw new ForbiddenException("User does not belong to this company");
        }

        return member.get();
    }
}
