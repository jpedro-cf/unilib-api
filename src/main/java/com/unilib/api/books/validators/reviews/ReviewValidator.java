package com.unilib.api.books.validators.reviews;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.repositories.ReviewsRepository;
import com.unilib.api.books.validators.books.PreviouslyBorrowedBooksValidator;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.books.validators.dto.ReviewValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.ConflictException;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewValidator implements Validator<ReviewValidation, Book> {
    private final PreviouslyBorrowedBooksValidator previouslyBorrowed;
    private final BooksRepository booksRepository;
    private final ReviewsRepository reviewsRepository;

    public ReviewValidator(PreviouslyBorrowedBooksValidator previouslyBorrowed,
                           BooksRepository booksRepository,
                           ReviewsRepository reviewsRepository){
        this.previouslyBorrowed = previouslyBorrowed;
        this.booksRepository = booksRepository;
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public Book validate(ReviewValidation request) {
        boolean alreadyReviewed = this.reviewsRepository
                .findByUserIdAndBookId(request.userId(), request.data().bookId())
                .isPresent();

        if(alreadyReviewed){
            throw new ConflictException("Você já fez uma review para esse livro.");
        }

        List<Borrow> borrowed = previouslyBorrowed.validate(new BookBorrowValidation(
                request.data().bookId(), request.userId()));

        boolean validBorrowStatus = borrowed.stream()
                .anyMatch(b -> !b.getStatus().equals(BorrowStatus.WAITING));

        if(!validBorrowStatus){
            throw new ForbiddenException("Você ainda não pode adicionar uma review para esse livro.");
        }

        return booksRepository.findById(request.data().bookId())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));
    }
}
