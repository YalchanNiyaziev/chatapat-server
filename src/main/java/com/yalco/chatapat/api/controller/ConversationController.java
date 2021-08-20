package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.UserConversationDto;
import com.yalco.chatapat.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/{username}")
    public ResponseEntity<List<UserConversationDto>> getUserConversations(@PathVariable String username) {
        return ResponseEntity.ok(conversationService.getAllUserConversationsByUsername(username));
    }
}
