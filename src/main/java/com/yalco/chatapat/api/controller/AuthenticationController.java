package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.AuthenticationRequestDto;
import com.yalco.chatapat.dto.ChatUserRegistrationRequest;
import com.yalco.chatapat.security.AuthenticationService;
import com.yalco.chatapat.service.ChatUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ChatUserService chatUserService;

    public AuthenticationController(AuthenticationService authenticationService, ChatUserService chatUserService) {
        this.authenticationService = authenticationService;
        this.chatUserService = chatUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto credentials) {
        try {

            String jwt = authenticationService.authenticate(credentials);
            return ResponseEntity.ok()
                    .header("Access-Control-Expose-Headers", "Authorization") // TODO Remove if it is unneeded
                    .header(HttpHeaders.AUTHORIZATION, jwt)
                    .build();
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ChatUserRegistrationRequest userForRegistration) {
        chatUserService.registerChatUser(userForRegistration);
        return ResponseEntity.ok().build();
    }
}
