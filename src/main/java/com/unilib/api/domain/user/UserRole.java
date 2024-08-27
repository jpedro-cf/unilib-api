package com.unilib.api.domain.user;

public enum UserRole {
    ADMIN("admin"),
    MANAGER("manager"),
    EDITOR("editor"),
    USER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}