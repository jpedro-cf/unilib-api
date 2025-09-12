package com.unilib.api.books.validators.books;

import com.unilib.api.books.Borrow;
import com.unilib.api.books.repositories.BorrowedBooksRepository;
import com.unilib.api.books.validators.dto.AcceptBorrowValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BorrowActionValidator implements Validator<AcceptBorrowValidation, Borrow> {
    private final BorrowedBooksRepository borrowedBooksRepository;
    private final CompanyMemberExist memberExist;

    public BorrowActionValidator(BorrowedBooksRepository borrowedBooksRepository,
                                 ValidatorsFactory factory){
        this.borrowedBooksRepository = borrowedBooksRepository;
        this.memberExist = factory.getValidator(CompanyMemberExist.class);
    }

    @Override
    public Borrow validate(AcceptBorrowValidation request) {
        Borrow borrow = borrowedBooksRepository.findById(request.borrowId())
                .orElseThrow(() -> new NotFoundException("Borrow not found."));

        UUID companyId = borrow.getBook().getCompany().getId();

        memberExist.validate(new CompanyMemberValidation(companyId, request.userId()));

        return borrow;
    }
}
