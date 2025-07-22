package com.unilib.api.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(@NotNull String name,
                                 @Email @NotNull String email,
                                 @NotNull  String password) {
}
