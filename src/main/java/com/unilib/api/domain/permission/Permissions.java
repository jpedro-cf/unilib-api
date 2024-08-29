package com.unilib.api.domain.permission;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Permissions {
    private Boolean admin;
    private Boolean manager;
    private Boolean editor;
}
