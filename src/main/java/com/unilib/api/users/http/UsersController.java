package com.unilib.api.users.http;

import com.unilib.api.config.security.TokenAuthentication;
import com.unilib.api.users.User;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> registrate(@RequestBody @Valid RegisterRequestDTO data){
        return ResponseEntity.ok(this.usersService.registrate(data));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(TokenAuthentication authentication){
        return ResponseEntity.ok(this.usersService.getAll(authentication.getUser()));
    }
}
