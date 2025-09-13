package com.unilib.api.books.http;

import com.unilib.api.books.Category;
import com.unilib.api.books.dto.CategoryRequestDTO;
import com.unilib.api.books.services.CategoriesService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService){
        this.categoriesService = categoriesService;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequestDTO data,
                                           TokenAuthentication authentication){

        Category category = this.categoriesService
                .createCategory(data, authentication.getUser());

        return ResponseEntity.ok(category);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByID(@PathVariable("id") UUID id){
        Category category = this.categoriesService.getByID(id);

        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<Page<Category>> getAll(Pageable pageable){
        Page<Category> categories = this.categoriesService.getCategories(pageable);
        return ResponseEntity.ok(categories);

    }

}
