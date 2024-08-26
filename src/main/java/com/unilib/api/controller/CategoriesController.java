package com.unilib.api.controller;

import com.unilib.api.domain.book.BookResponseDTO;
import com.unilib.api.domain.category.Category;
import com.unilib.api.domain.category.CategoryRequestDTO;
import com.unilib.api.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories/")
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;

    @PostMapping("/")
    public ResponseEntity<Category> create(@RequestBody CategoryRequestDTO data){

        Category category = this.categoriesService.create(data);

        return ResponseEntity.ok(category);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByID(@PathVariable UUID id){
        Category category = this.categoriesService.getByID(id);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/")
    public ResponseEntity<List<Category>> getMany(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "DESC") String sort,
                                  @RequestParam(defaultValue = "") String title){
        List<Category> categories = this.categoriesService.getCategories(title, sort, page, size);
        return ResponseEntity.ok(categories);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable UUID id,
                                           @RequestBody CategoryRequestDTO data
                                           ){
        Category category = this.categoriesService.update(id, data.title(), data.description());
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.categoriesService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

}
