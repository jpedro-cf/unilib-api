package com.unilib.api.companies;

import lombok.Getter;

@Getter
public enum CompanyRole {
    OWNER(2),
    ADMIN(1),
    MANAGER(0);

    private final int level;
    CompanyRole(int level){
        this.level = level;
    }
}
