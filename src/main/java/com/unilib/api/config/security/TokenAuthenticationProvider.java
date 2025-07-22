package com.unilib.api.config.security;

import com.unilib.api.shared.exceptions.UnauthorizedException;
import com.unilib.api.users.User;
import com.unilib.api.users.repositories.UsersRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final JwtDecoder decoder;
    private final UsersRepository usersRepository;

    public TokenAuthenticationProvider(JwtDecoder decoder,
                                       UsersRepository usersRepository){
        this.decoder = decoder;
        this.usersRepository = usersRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            TokenAuthentication tokenAuth = (TokenAuthentication) authentication;

            Jwt jwt = decoder.decode(tokenAuth.getToken());
            String subject = jwt.getSubject();

            Optional<User> user = this.usersRepository.findById(UUID.fromString(subject));
            if(user.isPresent()){
                tokenAuth.setUser(user.get());
                tokenAuth.setAuthenticated(true);
            }
            return tokenAuth;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
