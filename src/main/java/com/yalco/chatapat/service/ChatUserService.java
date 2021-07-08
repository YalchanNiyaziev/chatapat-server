package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.entity.BaseUser;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.utils.ObjectConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public ChatUserService(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    public List<ChatUserDto> getALlChatUsers() {
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers();
        return ObjectConverter.convertList(chatUsers, ChatUserDto.class);
    }
}
