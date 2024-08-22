package com.unilib.api.service;


import com.unilib.api.domain.category.BooksCategory;
import com.unilib.api.domain.category.CategoryRequestDTO;
import com.unilib.api.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository repository;
    public BooksCategory create(CategoryRequestDTO data){
        BooksCategory newCategory = new BooksCategory();

        newCategory.setTitle(data.title());
        newCategory.setDescription(data.description());

        this.repository.save(newCategory);

        return newCategory;
    }

    public Void deleteCategory(UUID id){
        this.repository.delete(this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));

        return null;
    }
}
