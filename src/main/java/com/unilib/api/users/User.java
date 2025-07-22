package com.unilib.api.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unilib.api.books.Borrow;
import com.unilib.api.books.Review;
import com.unilib.api.companies.CompanyMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "users") @Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CompanyMember> memberships;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private List<Borrow> borrows;
}
