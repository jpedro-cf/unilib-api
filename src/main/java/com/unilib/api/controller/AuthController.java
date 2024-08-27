package com.unilib.api.controller;

import com.unilib.api.domain.user.LoginRequestDTO;
import com.unilib.api.domain.user.LoginResponseDTO;
import com.unilib.api.domain.user.RegisterRequestDTO;
import com.unilib.api.domain.user.User;
import com.unilib.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse httpResponse){
        LoginResponseDTO response = this.authService.login(request);

        Cookie refreshTokenCookie = new Cookie("refresh_token", response.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // True se estiver usando HTTPS
        refreshTokenCookie.setPath("/api/refresh-token"); // Defina o caminho apropriado
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Exemplo: 30 dias
        httpResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequestDTO request){
        User response = this.authService.register(request);

        return ResponseEntity.ok(response);
    }

}
