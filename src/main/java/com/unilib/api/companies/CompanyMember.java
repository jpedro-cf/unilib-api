package com.unilib.api.companies;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private CompanyRole role;
}
