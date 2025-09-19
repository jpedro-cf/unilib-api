package com.unilib.api.users.dto;

import jakarta.validation.constraints.NotNull;

public record PasswordChangeDTO(@NotNull String newPassword,
                                @NotNull String oldPassword) {
}
