package com.unilib.api.service;

import com.unilib.api.domain.user.*;
import com.unilib.api.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterRequestDTO request){

        Optional<User> userFromDb = this.usersRepository.findByEmail(request.email());

        if(userFromDb.isPresent()){
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(UserRole.USER.name()));

        this.usersRepository.save(user);

        return user;
    }


    public LoginResponseDTO login(LoginRequestDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var accessToken = this.tokenService.generateAccessToken((User) auth.getPrincipal());
        var refreshToken = this.tokenService.generateRefreshToken((User) auth.getPrincipal());

        return new LoginResponseDTO(accessToken, refreshToken);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository.findDetailsByEmail(username);
    }

}
