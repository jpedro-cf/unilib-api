package com.unilib.api.users.validators;

import com.unilib.api.shared.Validator;
import com.unilib.api.shared.exceptions.ConflictException;
import com.unilib.api.shared.exceptions.InvalidArgumentException;
import com.unilib.api.users.User;
import com.unilib.api.users.dto.PasswordChangeDTO;
import com.unilib.api.users.repositories.UsersRepository;
import com.unilib.api.users.validators.dto.UpdateUserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserValidator implements Validator<UpdateUserValidation, Void> {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserValidator(UsersRepository usersRepository,
                               PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Void validate(UpdateUserValidation request) {
        if(request.data().email() != null){
            String newEmail = request.data().email();

            boolean emailInUse = this.usersRepository.findByEmail(newEmail).isPresent();
            if(emailInUse && !newEmail.equals(request.user().getEmail())){
                throw new ConflictException("E-mail já em uso.");
            }
        }

        if(request.data().passwordChange().isPresent()){
            PasswordChangeDTO passwordChange = request.data().passwordChange().get();
            User user = request.user();

            boolean passwordMatch = this.passwordEncoder.matches(passwordChange.oldPassword(),
                    user.getPassword());
            if(!passwordMatch){
                throw new InvalidArgumentException("Invalid password.");
            }
        }

        return null;

    }
}
