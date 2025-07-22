package com.unilib.api.users.validators;

import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.NotFoundException;
import com.unilib.api.users.User;
import com.unilib.api.users.repositories.UsersRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserExist implements Validator<UUID, User> {
    private final UsersRepository usersRepository;

    public UserExist(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }
    @Override
    public User validate(UUID request) {
        return usersRepository.findById(request)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }
}
