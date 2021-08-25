package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.SearchChatUserDto;
import com.yalco.chatapat.service.ChatUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // TODO make validation than only proncipal with same username can call this endpoint, because this fetch all user info
    //  OR create another andpoint to fetch self info !!!
    @GetMapping("/users/{username}")
    public ResponseEntity<ChatUserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getChatUseDtoByUsername(username));
    }

    @PostMapping("/users/search")
    public ResponseEntity<List<ChatUserDto>> search(SearchChatUserDto search) {
        return ResponseEntity.ok(userService.searchChatUser(search));
    }

    //TODO add functionality to edit user profile and change Pass AND RESET PASS AND SEND WELCOME EMAIL
}
