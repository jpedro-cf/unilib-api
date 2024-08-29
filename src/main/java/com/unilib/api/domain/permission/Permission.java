package com.unilib.api.domain.permission;

import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "permission")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Embedded
    private Permissions permissions;

}
