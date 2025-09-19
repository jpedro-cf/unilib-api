package com.unilib.api.users.validators.dto;

import com.unilib.api.users.User;
import com.unilib.api.users.dto.UpdateUserDTO;

public record UpdateUserValidation(UpdateUserDTO data, User user) {
}
