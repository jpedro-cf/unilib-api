package com.unilib.api.books.validators.books;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReadBookValidator implements Validator<BookBorrowValidation, Book> {
    private final BooksRepository booksRepository;
    private final PreviouslyBorrowedBooksValidator previouslyBorrowed;
    private final CompanyMemberExist memberExist;

    public ReadBookValidator(ValidatorsFactory factory, BooksRepository booksRepository){
        this.booksRepository = booksRepository;
        this.previouslyBorrowed = factory
                .getValidator(PreviouslyBorrowedBooksValidator.class);
        this.memberExist = factory
                .getValidator(CompanyMemberExist.class);
    }

    @Override
    public Book validate(BookBorrowValidation request) {
        Book book = booksRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found."));

        boolean memberOfCompany = memberExist
                .validate(new CompanyMemberValidation(book.getCompany().getId(), request.userId())) != null;

        if(memberOfCompany){
            return book;
        }

        List<Borrow> borrowedBooks = previouslyBorrowed.validate(request);
        boolean inProgress = borrowedBooks
                .stream().anyMatch(b -> b.getStatus().equals(BorrowStatus.IN_PROGRESS));

        if(!inProgress){
            throw new ForbiddenException("You don't have any borrowed book in progress.");
        }

        return book;
    }
}
