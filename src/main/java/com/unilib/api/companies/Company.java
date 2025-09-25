package com.unilib.api.companies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unilib.api.books.Book;
import com.unilib.api.books.Borrow;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Table(name = "company") @Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text", nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "company",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private Set<Book> books;

    @OneToMany(mappedBy = "company",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private Set<Group> groups;

    @OneToMany(mappedBy = "company",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JsonIgnore
    private Set<CompanyMember> members;
}
