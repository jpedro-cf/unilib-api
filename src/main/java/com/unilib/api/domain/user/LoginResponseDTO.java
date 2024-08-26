package com.unilib.api.domain.user;

public record LoginResponseDTO(String accessToken, Long expiresIn) {
}