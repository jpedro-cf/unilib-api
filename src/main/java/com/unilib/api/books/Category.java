package com.unilib.api.books;

import jakarta.persistence.*;
import lombok.*;

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
}
