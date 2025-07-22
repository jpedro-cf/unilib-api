package com.unilib.api.users.services;

import com.unilib.api.config.security.TokenAuthentication;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.shared.exceptions.UnauthorizedException;
import com.unilib.api.users.repositories.UsersRepository;
import com.unilib.api.shared.TokenService;
import com.unilib.api.users.User;
import com.unilib.api.users.dto.LoginRequestDTO;
import com.unilib.api.users.dto.LoginResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class AuthService {

    private final TokenService tokenService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(TokenService tokenService,
                       UsersRepository usersRepository,
                       PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = this.usersRepository.findByEmail(request.email())
                .orElseThrow(() -> new ForbiddenException("Invalid e-mail or password."));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new ForbiddenException("Invalid e-mail or password.");
        }

        String accessToken = this.tokenService.encode(
                Map.of("subject", user.getId().toString()),
                Duration.ofDays(5).getSeconds());

        return new LoginResponseDTO(user ,accessToken);
    }

    public User getCurrentUser(){
        TokenAuthentication authentication = (TokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(authentication == null){
            throw new UnauthorizedException("You're not authenticated.");
        }

        return authentication.getUser();
    }

}
