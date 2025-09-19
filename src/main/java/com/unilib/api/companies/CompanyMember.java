package com.unilib.api.companies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unilib.api.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Table(name = "company_member")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class CompanyMember {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonManagedReference
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyRole role;
}
