package com.unilib.api.users.dto;

import com.unilib.api.users.User;

public record LoginResponseDTO(User user, String accessToken) {
}