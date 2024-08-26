package com.unilib.api.service;


import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.category.Category;
import com.unilib.api.domain.category.CategoryRequestDTO;
import com.unilib.api.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository repository;

    public Category create(CategoryRequestDTO data){
        Category newCategory = new Category();

        newCategory.setTitle(data.title());
        newCategory.setDescription(data.description());

        this.repository.save(newCategory);

        return newCategory;
    }

    public Category getByID(UUID id)
    {
        return this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public List<Category> getCategories(String title, String sort, int page, int size){
        title = (title != null) ? title : "";

        Sort sortOrder = Sort.by(Sort.Direction.ASC, "title"); // Default sorting
        if ("desc".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "title");
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Category> categoriesPage = this.repository.findFilteredCategories(title, pageable);

        return categoriesPage.getContent();
    }

    public Category update(UUID id, String title, String description){
        Optional<Category> existingCategory = this.repository.findById(id);

        if(existingCategory.isEmpty()){
            throw new IllegalArgumentException("Category not found");
        }

        Category category = existingCategory.get();

        if(title != null){
            category.setTitle(title);
        }

        if(description != null){
            category.setDescription(description);
        }

        this.repository.save(category);

        return category;
    }

    public Void deleteCategory(UUID id){
        this.repository.delete(this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));

        return null;
    }
}
