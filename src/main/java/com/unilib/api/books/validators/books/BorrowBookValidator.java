package com.unilib.api.books.validators.books;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BorrowBookValidator implements Validator<BookBorrowValidation, Book> {
    private final BooksRepository booksRepository;
    private final PreviouslyBorrowedBooksValidator previouslyBorrowed;

    public BorrowBookValidator(ValidatorsFactory factory,BooksRepository booksRepository){
        this.booksRepository = booksRepository;
        this.previouslyBorrowed = factory
                .getValidator(PreviouslyBorrowedBooksValidator.class);
    }

    @Override
    public Book validate(BookBorrowValidation request) {
        List<Borrow> borrowedBooks = previouslyBorrowed
                .validate(request);

        boolean invalidState = borrowedBooks.stream()
                .anyMatch(b -> !b.getStatus().equals(BorrowStatus.COMPLETED));

        if(invalidState){
            throw new ForbiddenException("Você já pegou esse livro emprestado. Espere o prazo terminar.");
        }

        return booksRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));
    }
}
