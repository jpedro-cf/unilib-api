package com.unilib.api.domain.book;
import com.unilib.api.domain.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Table(name = "book")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private String image;
    private String pdf;
    private Date createdAt;

    @ManyToMany
    @JoinTable(
            name = "book_category", // Name of the join table
            joinColumns = @JoinColumn(name = "book_id"), // Foreign key for Book in join table
            inverseJoinColumns = @JoinColumn(name = "category_id") // Foreign key for Category in join table
    )
    private Set<Category> categories;
}
