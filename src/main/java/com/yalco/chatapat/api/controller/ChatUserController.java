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

    @PostMapping("/users/search")
    public ResponseEntity<List<ChatUserDto>> search(SearchChatUserDto search) {
        return ResponseEntity.ok(userService.searchChatUser(search));
    }

    @GetMapping("/users/{userId}/contacts")
    public ResponseEntity<List<ChatUserDto>> getContacts(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/contacts/{removedUserId}")
    public ResponseEntity<Void> removeContact(@PathVariable String userId, @PathVariable String removedUserId) {
        return null;
    }

    @PostMapping("/users/{userId}/blocks/{blockedId}")
    public ResponseEntity<Void> blockUser(@PathVariable String userId, @PathVariable String blockedId) {
        return null;
    }

    @DeleteMapping("/users/{userId}/blocks/{blockedId}")
    public ResponseEntity<Void> unblockUser(@PathVariable String userId, @PathVariable String blockedId) {
        return null;
    }

    @GetMapping("/users/{userId}/pending-contacts")
    public ResponseEntity<List<ChatUserDto>> getPendingContacts(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/pending-contacts")
    public ResponseEntity<List<ChatUserDto>> sendContactRequest(@PathVariable String userId, @RequestBody ChatUserDto contactInfo) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/pending-contacts/{acceptedUserId}")
    public ResponseEntity<Void> acceptContactRequest(@PathVariable String userId, @PathVariable String acceptedUserId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/pending-contacts/{rejectUserId}")
    public ResponseEntity<Void> rejectContactRequest(@PathVariable String userId, @PathVariable String rejectUserId) {
        return ResponseEntity.ok().build();
    }

    //send contact request
    //list of all pending request
    //approve OR reject contact request
    //remove contact
    //block contact
}
