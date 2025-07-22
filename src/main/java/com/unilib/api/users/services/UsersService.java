package com.unilib.api.users.services;

import com.unilib.api.users.User;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.repositories.UsersRepository;
import com.unilib.api.users.validators.RegistrationValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RegistrationValidation registrationValidation;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        RegistrationValidation registrationValidation,
                        PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.registrationValidation = registrationValidation;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrate(RegisterRequestDTO request){
        registrationValidation.validate(request);

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return this.usersRepository.save(user);
    }
}
