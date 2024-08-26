package com.unilib.api.repositories;


import com.unilib.api.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c " +
            "WHERE (:title = '' OR c.title LIKE %:title%)")
    Page<Category> findFilteredCategories(@Param("title") String title, Pageable pageable);
}