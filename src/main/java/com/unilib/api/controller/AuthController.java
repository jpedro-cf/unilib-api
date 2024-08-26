package com.unilib.api.controller;

import com.unilib.api.domain.user.LoginRequestDTO;
import com.unilib.api.domain.user.LoginResponseDTO;
import com.unilib.api.domain.user.RegisterRequestDTO;
import com.unilib.api.domain.user.User;
import com.unilib.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
        LoginResponseDTO response = this.authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequestDTO request){
        User response = this.authService.register(request);

        return ResponseEntity.ok(response);
    }
}
