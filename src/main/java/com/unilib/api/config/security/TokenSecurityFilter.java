package com.unilib.api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TokenSecurityFilter extends OncePerRequestFilter {
    private final AuthenticationManager manager;

    public TokenSecurityFilter(AuthenticationManager manager){
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);

        if(token != null){
            TokenAuthentication authentication = new TokenAuthentication(token);

            manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }

        List<Cookie> cookie = Arrays
                .stream(cookies)
                .filter(c -> c.getName().equals("access_token"))
                .toList();

        if(cookie.isEmpty()){
            return null;
        }
        return cookie.getFirst().getValue();
    }
}