package com.unilib.api.books.services;


import com.unilib.api.books.Book;
import com.unilib.api.books.Category;
import com.unilib.api.books.dto.CategoryRequestDTO;
import com.unilib.api.books.repositories.CategoriesRepository;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.shared.exceptions.ConflictException;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public Category createCategory(CategoryRequestDTO data, User user){
        boolean canCreate = user.getMemberships()
                .stream()
                .anyMatch(m -> m.getRole().getLevel() >= CompanyRole.ADMIN.getLevel());

        if(!canCreate){
            throw new ForbiddenException("You can't create a category.");
        }

        boolean exists = this.categoriesRepository.findByTitle(data.title()).isPresent();
        if(exists){
            throw new ConflictException("Category already exists.");
        }

        Category newCategory = Category.builder()
                .title(data.title())
                .description(data.description())
                .build();

        this.categoriesRepository.save(newCategory);

        return newCategory;
    }

    public Category getByID(UUID id)
    {
        return this.categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public Page<Category> getCategories(Pageable pageable){
        return this.categoriesRepository.findAll(pageable);
    }

    public void deleteCategory(UUID categoryId, User user){
        boolean canDelete = user.getMemberships()
                .stream()
                .anyMatch(m -> m.getRole().getLevel() >= CompanyRole.ADMIN.getLevel());
        if(!canDelete){
            throw new ForbiddenException("You can't delete this category.");
        }

        Category category = this.categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category does not exist."));

        for(Book book: new ArrayList<>(category.getBooks())){
            book.getCategories().remove(category);
        }

        this.categoriesRepository.deleteById(categoryId);
    }
}
