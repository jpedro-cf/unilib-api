package com.unilib.api.domain.company;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Table(name = "company")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    private String image;

    private Date createdAt;
}
