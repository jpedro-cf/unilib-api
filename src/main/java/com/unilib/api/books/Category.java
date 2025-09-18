package com.unilib.api.books;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "categories") @Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String title;
    private String description;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<Book> books;
}
