package com.unilib.api.users.validators;

import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.ConflictException;
import com.unilib.api.users.User;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.repositories.UsersRepository;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidation implements Validator<RegisterRequestDTO, Void> {
    private final UsersRepository usersRepository;

    public RegistrationValidation(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public Void validate(RegisterRequestDTO request) {
        if(this.usersRepository.findByEmail(request.email()).isPresent()){
            throw new ConflictException("E-mail já em uso.");
        }

        return null;
    }
}
