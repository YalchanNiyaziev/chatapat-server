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

    @GetMapping("/users/{userId}/connections")
    public ResponseEntity<List<ChatUserDto>> getConnections(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/connections/{removedUserId}")
    public ResponseEntity<Void> removeConnection(@PathVariable String userId, @PathVariable String removedUserId) {
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

    @GetMapping("/users/{userId}/pending-connections")
    public ResponseEntity<List<ChatUserDto>> getPendingConnections(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{senderName}/pending-connections")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable String senderName, @RequestBody ChatUserDto contactInfo) {
        userService.sendConnectionRequest(senderName, contactInfo.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/pending-connections/{acceptedUserId}")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable String userId, @PathVariable String acceptedUserId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/pending-connection/{rejectUserId}")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable String userId, @PathVariable String rejectUserId) {
        return ResponseEntity.ok().build();
    }

    //send contact request
    //list of all pending request
    //approve OR reject contact request
    //remove contact
    //block contact
}
