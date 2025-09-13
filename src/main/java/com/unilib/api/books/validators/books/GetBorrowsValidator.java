package com.unilib.api.books.validators.books;

import com.unilib.api.books.dto.GetBorrowsDTO;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import org.springframework.stereotype.Component;

@Component
public class GetBorrowsValidator implements Validator<GetBorrowsDTO, Void> {
    private final CompanyMemberExist memberExist;

    public GetBorrowsValidator(ValidatorsFactory factory){
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
    }

    @Override
    public Void validate(GetBorrowsDTO request) {
        if(request.companyId().isEmpty()){
            return null;
        }

        memberExist.validate(new CompanyMemberValidation(request.companyId().get(),
                request.user().getId()));

        return null;
    }
}
