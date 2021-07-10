package com.yalco.chatapat.security;

import com.yalco.chatapat.dto.AuthenticationRequestDto;
import com.yalco.chatapat.security.jwt.JwtConfiguration;
import com.yalco.chatapat.security.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    private final JwtProvider jwtProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.jwtProvider = jwtProvider;
    }

    public String authenticate(AuthenticationRequestDto credentials) {
//TODO Remove try cath from controller add the here and throw custom exception
//        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return String.format("%s %s",
                    jwtConfiguration.getTokenPrefix(),
                    jwtProvider.generateToken(authentication));
//        } catch (AuthenticationException ex) {
//            throw new WrongCredentialsException("Credential details are wrong");
//        }

    }
}
