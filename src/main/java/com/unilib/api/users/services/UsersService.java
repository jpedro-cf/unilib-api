package com.unilib.api.users.services;

import com.unilib.api.shared.ValidatorsFactory;
import com.unilib.api.shared.exceptions.ForbiddenException;
import com.unilib.api.users.User;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.dto.UpdateUserDTO;
import com.unilib.api.users.repositories.UsersRepository;
import com.unilib.api.users.validators.RegistrationValidation;
import com.unilib.api.users.validators.UpdateUserValidator;
import com.unilib.api.users.validators.dto.UpdateUserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final ValidatorsFactory validatorsFactory;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        ValidatorsFactory validatorsFactory,
                        PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.validatorsFactory = validatorsFactory;
    }

    public User registrate(RegisterRequestDTO request){
        RegistrationValidation validator = validatorsFactory
                .getValidator(RegistrationValidation.class);

        validator.validate(request);

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return this.usersRepository.save(user);
    }

    public List<User> getAll(User user){
        if(user.getMemberships().isEmpty()){
            throw new ForbiddenException("You're not allowed to view users.");
        }

        return this.usersRepository.findAll();
    }

    public User update(UpdateUserDTO data, User user){
        UpdateUserValidator validator = validatorsFactory
                .getValidator(UpdateUserValidator.class);

        validator.validate(new UpdateUserValidation(data, user));

        user.setName(data.name().orElse(user.getName()));
        user.setEmail(data.email() == null ? user.getEmail() : data.email());

        if(data.passwordChange().isPresent()){
            String newPassword = passwordEncoder
                    .encode(data.passwordChange().get().newPassword());

            user.setPassword(newPassword);
        }

        return this.usersRepository.save(user);
    }
}
