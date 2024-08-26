package com.unilib.api.service;

import com.unilib.api.domain.role.Role;
import com.unilib.api.domain.user.LoginRequestDTO;
import com.unilib.api.domain.user.LoginResponseDTO;
import com.unilib.api.domain.user.RegisterRequestDTO;
import com.unilib.api.domain.user.User;
import com.unilib.api.repositories.RolesRepository;
import com.unilib.api.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterRequestDTO request){
        Role basicRole = this.rolesRepository.findByName(Role.Values.user.name());

        Optional<User> userFromDb = this.usersRepository.findByEmail(request.email());

        if(userFromDb.isPresent()){
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(basicRole));

        this.usersRepository.save(user);

        return user;
    }


    public LoginResponseDTO login(LoginRequestDTO request){
         Optional<User> user = this.usersRepository.findByEmail(request.email());

        if(!user.isPresent()){
            throw new BadCredentialsException("user or password is invalid!");
        }

        if(!this.isLoginCorrect(request, user.get().getPassword())){
            throw new BadCredentialsException("user or password is invalid!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("unilib-api")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue, expiresIn);
    }

    private boolean isLoginCorrect(LoginRequestDTO request, String password) {
        return this.passwordEncoder.matches(request.password(), password);
    }


}
