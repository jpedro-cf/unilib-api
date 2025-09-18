package com.unilib.api.books.validators.books;

import com.unilib.api.books.Book;
import com.unilib.api.books.Category;
import com.unilib.api.books.repositories.BooksRepository;
import com.unilib.api.books.validators.dto.UpdateBookValidation;
import com.unilib.api.companies.Company;
import com.unilib.api.companies.validators.company.CompanyExist;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateBookValidator implements Validator<UpdateBookValidation, Book> {
    private final ValidatorsFactory factory;
    private final CategoriesExist categoriesExists;
    private final CompanyMemberExist memberExists;
    private final BooksRepository booksRepository;

    public UpdateBookValidator(ValidatorsFactory factory, BooksRepository booksRepository){
        this.factory = factory;
        this.booksRepository = booksRepository;
        this.categoriesExists = factory.getValidator(CategoriesExist.class);
        this.memberExists = factory.getValidator(CompanyMemberExist.class);
    }
    @Override
    public Book validate(UpdateBookValidation request) {
        Book book = booksRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found."));

        memberExists.validate(new CompanyMemberValidation(book.getCompany().getId(),
                request.user().getId()));

        if(request.data().categories().isPresent()){
            List<Category> categories = categoriesExists
                    .validate(request.data().categories().get());

            book.setCategories(categories);
        }

        return book;
    }
}
