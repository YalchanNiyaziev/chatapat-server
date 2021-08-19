package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.UserPendingConnectionDto;
import com.yalco.chatapat.service.UserConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-connections")
@RequiredArgsConstructor
public class UserConnectionController {

    private final UserConnectionService connectionService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ChatUserDto>> getConnections(@PathVariable String userId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{username}-{removedUsername}")
    public ResponseEntity<Void> removeConnection(@PathVariable String username, @PathVariable String removedUsername) {
        connectionService.removeUserConnection(username, removedUsername);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{username}-{blockedUsername}/blocks")
    public ResponseEntity<Void> blockUser(@PathVariable String username, @PathVariable String blockedUsername) {
        connectionService.blockUserConnection(username, blockedUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{username}-{unblockedUsername}/blocks")
    public ResponseEntity<Void> unblockUser(@PathVariable String username, @PathVariable String unblockedUsername) {
        connectionService.unblockUserConnection(username, unblockedUsername);
        return ResponseEntity.ok().build();
    }

    //TODO write test
    @GetMapping("/users/{username}/pending-connections")
    public ResponseEntity<List<UserPendingConnectionDto>> getPendingConnections(@PathVariable String username) {
        return ResponseEntity.ok(connectionService.getAllPendingConnectionRequest(username));
    }

    @PostMapping("/users/{senderName}/pending-connections")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable String senderName, @RequestBody ChatUserDto contactInfo) {
        connectionService.sendConnectionRequest(senderName, contactInfo.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{username}-{acceptedUsername}/pending-connections")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable String username, @PathVariable String acceptedUsername) {
        connectionService.acceptConnectionRequest(username, acceptedUsername);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{username}-{rejectUsername}/pending-connection")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable String username, @PathVariable String rejectUsername) {
        connectionService.rejectConnectionRequest(username, rejectUsername);
        return ResponseEntity.ok().build();
    }

    //send contact request
    //list of all pending request
    //approve OR reject contact request
    //remove contact
    //block contact

}
