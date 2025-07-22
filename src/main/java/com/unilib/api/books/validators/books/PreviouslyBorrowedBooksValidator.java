package com.unilib.api.books.validators.books;

import com.unilib.api.books.Borrow;
import com.unilib.api.books.repositories.BorrowedBooksRepository;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.shared.Validator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreviouslyBorrowedBooksValidator implements Validator<BookBorrowValidation, List<Borrow>> {
    private final BorrowedBooksRepository borrowedBooksRepository;

    public PreviouslyBorrowedBooksValidator(BorrowedBooksRepository borrowedBooksRepository){
        this.borrowedBooksRepository = borrowedBooksRepository;
    }

    @Override
    public List<Borrow> validate(BookBorrowValidation request) {
        return borrowedBooksRepository
                .getByBookIdAndUserId(request.bookId(), request.userId());
    }
}
