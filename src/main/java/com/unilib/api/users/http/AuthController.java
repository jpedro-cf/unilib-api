package com.unilib.api.users.http;

import com.unilib.api.users.dto.LoginRequestDTO;
import com.unilib.api.users.dto.LoginResponseDTO;
import com.unilib.api.users.dto.RegisterRequestDTO;
import com.unilib.api.users.User;
import com.unilib.api.users.services.AuthService;
import com.unilib.api.users.services.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request,
                                                  HttpServletResponse response){
        LoginResponseDTO login = this.authService.login(request);

        HttpCookie cookie = ResponseCookie.from("access_token", login.accessToken())
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(login);
    }

    @GetMapping("/me")
    public ResponseEntity<User> current(){
        return ResponseEntity.ok(this.authService.getCurrentUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        HttpCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

}
