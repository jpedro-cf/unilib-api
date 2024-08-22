package com.unilib.api.repositories;


import com.unilib.api.domain.category.BooksCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<BooksCategory, UUID> {
}