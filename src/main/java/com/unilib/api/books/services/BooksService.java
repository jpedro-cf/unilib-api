package com.unilib.api.books.services;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.dto.*;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.repositories.BorrowedBooksRepository;
import com.unilib.api.books.validators.books.*;
import com.unilib.api.books.validators.dto.AcceptBorrowValidation;
import com.unilib.api.books.validators.dto.AddBookValidation;
import com.unilib.api.books.validators.dto.BookBorrowValidation;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Storage;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.InvalidArgumentException;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class BooksService {
    private final BooksRepository booksRepository;
    private final BorrowedBooksRepository borrowedBooksRepository;
    private final ValidatorsFactory validatorsFactory;
    private final Storage storage;

    public BooksService(BooksRepository booksRepository,
                        BorrowedBooksRepository borrowedBooksRepository,
                        ValidatorsFactory validatorsFactory,
                        Storage storage){
        this.booksRepository = booksRepository;
        this.borrowedBooksRepository = borrowedBooksRepository;
        this.validatorsFactory = validatorsFactory;
        this.storage = storage;
    }

    public Book addBook(AddBookRequestDTO data, User user) throws Exception {
        AddBookValidator validator = validatorsFactory.getValidator(AddBookValidator.class);
        Book newBook = validator.validate(new AddBookValidation(data,user));

        newBook.setImage("images/" + UUID.randomUUID());
        newBook.setPdf("pdf/" + UUID.randomUUID());

        storage.uploadObject(newBook.getImage(), data.image().getBytes(), Map.of());
        storage.uploadObject(newBook.getPdf(), data.pdf().getBytes(), Map.of());

        this.booksRepository.save(newBook);

        return newBook;
    }

    public Book getByID(UUID id){
        return this.booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

    }

    public Page<Book> getBooks(Pageable pageable){
        return this.booksRepository.findAll(pageable);
    }

    public void deleteBook(UUID bookId, User user){
        Book book = this.booksRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found."));

        CompanyMemberExist validator = validatorsFactory
                .getValidator(CompanyMemberExist.class);

        validator.validate(new CompanyMemberValidation(
                book.getCompany().getId(), user.getId()));

        this.booksRepository.deleteById(bookId);
    }

    public ReadBookResponse readBook(UUID bookId, User user){
        ReadBookValidator validator = validatorsFactory.getValidator(ReadBookValidator.class);
        Book book = validator.validate(new BookBorrowValidation(bookId, user.getId()));

        String url = storage.generateSignedUrl(book.getPdf());

        return new ReadBookResponse(url, book);
    }

    public Borrow borrowBook(UUID bookId, BorrowBookDTO request, User user){
        if(request.expiration().isBefore(request.release())){
            throw new InvalidArgumentException("Expiration date can't come before the release date.");
        }

        if(request.expiration().isBefore(Instant.now())){
            throw new InvalidArgumentException("Expiration date can't come before the actual date.");
        }

        BorrowBookValidator validator = validatorsFactory
                .getValidator(BorrowBookValidator.class);

        Book book = validator
                .validate(new BookBorrowValidation(bookId, user.getId()));

        Borrow data = Borrow.builder()
                .user(user)
                .book(book)
                .companyId(book.getCompany().getId())
                .status(BorrowStatus.WAITING)
                .releaseAt(request.release())
                .expiresAt(request.expiration())
                .build();

        return borrowedBooksRepository.save(data);
    }

    public List<BorrowedBookDTO> getBorrows(GetBorrowsDTO data){
        GetBorrowsValidator validator = validatorsFactory
                .getValidator(GetBorrowsValidator.class);

        validator.validate(data);


        List<Borrow> response = List.of();
        if(data.companyId().isPresent()){
            response = borrowedBooksRepository.findAllByCompanyId(data.companyId().get());
        }

        response = borrowedBooksRepository.getByUserId(data.user().getId());

        return response.stream()
                .map(BorrowedBookDTO::fromEntity)
                .toList();
    }

    public void acceptBorrow(UUID borrowId, User user){
        BorrowActionValidator validator = validatorsFactory
                .getValidator(BorrowActionValidator.class);

        Borrow borrow = validator
                .validate(new AcceptBorrowValidation(borrowId, user.getId()));

        borrow.accept();

        borrowedBooksRepository.save(borrow);
    }

    public void denyBorrow(UUID borrowId, User user){
        BorrowActionValidator validator = validatorsFactory
                .getValidator(BorrowActionValidator.class);

        Borrow borrow = validator
                .validate(new AcceptBorrowValidation(borrowId, user.getId()));

        borrowedBooksRepository.delete(borrow);
    }
}
