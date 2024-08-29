package com.unilib.api.domain.book;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unilib.api.domain.category.Category;
import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.review.Review;
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
}
