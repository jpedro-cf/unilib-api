package com.unilib.api.shared;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.unilib.api.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

@Service
public class TokenService {
    private final JwtDecoder decoder;
    private final JwtEncoder encoder;

    public TokenService(JwtDecoder decoder, JwtEncoder encoder){
        this.decoder = decoder;
        this.encoder = encoder;
    }

    public String encode(Map<String, String> data, long expiration) {
        Instant now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("unilib")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofSeconds(expiration)))
                .subject(data.get("subject"))
                .claims(c -> {
                    data.forEach((key, value) -> {
                        if (!key.equals("subject")) {
                            c.put(key, value);
                        }
                    });
                })
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Map<String, Object> decode(String value) {
        Jwt decoded = decoder.decode(value);

        return decoded.getClaims();
    }

}