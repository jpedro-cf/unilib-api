package com.unilib.api.books.validators.books;

import com.unilib.api.books.Book;
import com.unilib.api.books.Category;
import com.unilib.api.books.validators.dto.AddBookValidation;
import com.unilib.api.companies.Company;
import com.unilib.api.companies.validators.company.CompanyExist;
import com.unilib.api.companies.validators.dto.CompanyMemberValidation;
import com.unilib.api.companies.validators.member.CompanyMemberExist;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.ValidatorsFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class AddBookValidator implements Validator<AddBookValidation, Book> {
    private final CompanyMemberExist memberExists;
    private final CompanyExist companyExists;
    private final CategoriesExist categoriesExists;

    public AddBookValidator(ValidatorsFactory factory){
        this.memberExists = factory.getValidator(CompanyMemberExist.class);
        this.companyExists = factory.getValidator(CompanyExist.class);
        this.categoriesExists = factory.getValidator(CategoriesExist.class);
    }

    @Override
    public Book validate(AddBookValidation request) {
        Company company = companyExists.validate(request.data().companyId());

        memberExists.validate(new CompanyMemberValidation(
                request.data().companyId(), request.user().getId()));

        Book newBook = Book.builder()
                .title(request.data().title())
                .description(request.data().description())
                .company(company)
                .createdAt(Instant.now())
                .build();

        if(request.data().categories().isPresent()){
            List<Category> categories = categoriesExists
                    .validate(request.data().categories().get());

            newBook.setCategories(categories);
        }

        return newBook;
    }
}
