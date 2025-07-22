package com.unilib.api.books.services;


import com.unilib.api.books.Category;
import com.unilib.api.books.dto.CategoryRequestDTO;
import com.unilib.api.books.repositories.CategoriesRepository;
import com.unilib.api.companies.CompanyRole;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Category> getCategories(String sort, int page, int size){
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "title"); // Default sorting
        if ("desc".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "title");
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Category> categoriesPage = this.categoriesRepository
                .findAll(pageable);

        return categoriesPage.getContent();
    }
}
