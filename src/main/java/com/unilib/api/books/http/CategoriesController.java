package com.unilib.api.books.http;

import com.unilib.api.books.Category;
import com.unilib.api.books.dto.CategoryRequestDTO;
import com.unilib.api.books.services.CategoriesService;
import com.unilib.api.config.security.TokenAuthentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Category> getByID(@PathVariable UUID id){
        Category category = this.categoriesService.getByID(id);

        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getMany(@RequestParam(defaultValue = "0", required = false) int page,
                                  @RequestParam(defaultValue = "10", required = false) int size,
                                  @RequestParam(defaultValue = "DESC", required = false) String sort){
        List<Category> categories = this.categoriesService.getCategories(sort, page, size);
        return ResponseEntity.ok(categories);

    }

}
