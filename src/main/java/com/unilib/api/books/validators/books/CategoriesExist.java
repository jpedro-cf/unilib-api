package com.unilib.api.books.validators.books;

import com.unilib.api.books.Category;
import com.unilib.api.books.repositories.CategoriesRepository;
import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CategoriesExist implements Validator<List<UUID>, List<Category>> {
    private final CategoriesRepository categoriesRepository;

    public CategoriesExist(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<Category> validate(List<UUID> request) {
        List<Category> categories = categoriesRepository.findAllById(request);
        if(categories.size() != request.size()){
            throw new NotFoundException("Uma das categorias selecionadas não existe.");
        }
        return categories;
    }
}
