package com.unilib.api.users.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.util.Optional;

public record UpdateUserDTO(Optional<String> name,
                            @Email @Nullable String email,
                            @Valid Optional<PasswordChangeDTO> passwordChange) {
}
