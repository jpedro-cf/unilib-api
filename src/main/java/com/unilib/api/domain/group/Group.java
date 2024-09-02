package com.unilib.api.domain.group;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unilib.api.domain.company.Company;
import com.unilib.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Table(name = "groups")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonManagedReference
    private Company company;

    private Date createdAt;
}
