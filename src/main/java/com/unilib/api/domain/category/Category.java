package com.unilib.api.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unilib.api.domain.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "category")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<Book> books;
}
