package com.unilib.api.books.services;

import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.dto.AddBookRequestDTO;
import com.unilib.api.books.dto.BookResponseDTO;
import com.unilib.api.books.dto.BorrowBookDTO;
import com.unilib.api.books.dto.ReadBookResponse;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.repositories.BorrowedBooksRepository;
import com.unilib.api.books.validators.books.AcceptBorrowValidator;
import com.unilib.api.books.validators.books.BorrowBookValidator;
import com.unilib.api.books.validators.books.AddBookValidator;
import com.unilib.api.books.validators.books.ReadBookValidator;
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

    public List<BookResponseDTO> getBooks(String sort, int page, int size){
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "createdAt");
        if ("desc".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Book> booksPage = this.booksRepository.findAll(pageable);

        // Isso aq é pra no futuro retornar paginação (total_pages, total_items, etc)
        return booksPage.map(book -> new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getImage(),
                book.getPdf(),
                book.getCompany(),
                book.getReviews(),
                book.getCategories(),
                book.getCreatedAt()
        )).stream().toList();
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
                .status(BorrowStatus.WAITING)
                .releaseAt(request.release())
                .expiresAt(request.expiration())
                .build();

        return borrowedBooksRepository.save(data);
    }

    public void acceptBorrow(UUID borrowId, User user){
        AcceptBorrowValidator validator = validatorsFactory
                .getValidator(AcceptBorrowValidator.class);

        Borrow borrow = validator
                .validate(new AcceptBorrowValidation(borrowId, user.getId()));

        borrow.accept();

        borrowedBooksRepository.save(borrow);
    }
}
