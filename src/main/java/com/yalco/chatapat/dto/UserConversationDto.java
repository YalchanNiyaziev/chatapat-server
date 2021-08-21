package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConversationDto {
    private Long conversationId;
    private String firstName;
    private String surName;
    private String username;
    private String chatName;
    private String imageUrl;
    private ChatUserStatus status;
    private String lastMessage;
    private Instant lastMessageTs;
    private String lastMessageSenderUsername;
    private MessageStatus messageStatus;
}
