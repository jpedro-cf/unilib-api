package com.unilib.api.books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unilib.api.companies.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "books") @Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String image;

    @Column(nullable = false)
    private String pdf;

    @OneToMany(mappedBy = "book",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "book",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private List<Borrow> borrows;

    @Column(name = "created_at")
    private Instant createdAt;
}
