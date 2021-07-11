package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.service.ChatUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-management")
public class ChatUserController {

    private final ChatUserService userService;

    public ChatUserController(ChatUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<ChatUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getALlChatUsers());
    }
}
