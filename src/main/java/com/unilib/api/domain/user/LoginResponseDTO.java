package com.unilib.api.domain.user;

public record LoginResponseDTO(User user,String accessToken, String refreshToken) {
}