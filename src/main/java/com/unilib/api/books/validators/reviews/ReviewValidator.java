package com.unilib.api.books.validators.reviews;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.validators.books.PreviouslyBorrowedBooksValidator;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.books.validators.dto.ReviewValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewValidator implements Validator<ReviewValidation, Book> {
    private final PreviouslyBorrowedBooksValidator previouslyBorrowed;
    private final BooksRepository booksRepository;

    public ReviewValidator(PreviouslyBorrowedBooksValidator previouslyBorrowed,
                           BooksRepository booksRepository){
        this.previouslyBorrowed = previouslyBorrowed;
        this.booksRepository = booksRepository;
    }

    @Override
    public Book validate(ReviewValidation request) {
        List<Borrow> borrowed = previouslyBorrowed.validate(new BookBorrowValidation(
                request.data().bookId(), request.userId()));

        boolean validBorrowStatus = borrowed.stream()
                .anyMatch(b -> !b.getStatus().equals(BorrowStatus.WAITING));

        if(!validBorrowStatus){
            throw new ForbiddenException("You can't add a review to this book.");
        }

        return booksRepository.findById(request.data().bookId())
                .orElseThrow(() -> new NotFoundException("Book not found."));
    }
}
