package com.unilib.api.users.http;

import com.unilib.api.users.User;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
