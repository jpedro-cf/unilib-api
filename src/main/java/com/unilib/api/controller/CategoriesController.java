package com.unilib.api.controller;

import com.unilib.api.domain.category.BooksCategory;
import com.unilib.api.domain.category.CategoryRequestDTO;
import com.unilib.api.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books_categories/")
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;

    @PostMapping("/")
    public ResponseEntity<BooksCategory> create(@RequestBody CategoryRequestDTO data){

        BooksCategory category = this.categoriesService.create(data);

        return ResponseEntity.ok(category);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.categoriesService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

}
