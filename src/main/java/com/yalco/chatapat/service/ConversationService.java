package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.TextMessageDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.Conversation;
import com.yalco.chatapat.entity.ConversationMessage;
import com.yalco.chatapat.enums.ConversationType;
import com.yalco.chatapat.enums.MessageStatus;
import com.yalco.chatapat.enums.MessageType;
import com.yalco.chatapat.exception.InternalErrorException;
import com.yalco.chatapat.repository.ConversationMessageRepository;
import com.yalco.chatapat.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository messageRepository;
    private final ChatUserService chatUserService;


    public void saveUserSpecificTextMessage(TextMessageDto textMessage) {
        Conversation conversation = getConversationByParticipantUsernames(textMessage.getReceiverName(), textMessage.getSenderName());
        ConversationMessage message = new ConversationMessage();
        message.setContent(textMessage.getContent());
        message.setSender(chatUserService.findChatUserByUsername(textMessage.getSenderName()));
        message.setMessageTs(textMessage.getMessageTs() != null ? textMessage.getMessageTs() : Instant.now());
        message.setType(MessageType.TEXT);
        message.setStatus(MessageStatus.SEND);
        message.setConversation(conversation);

        messageRepository.save(message);
    }


    private Conversation getConversationByParticipantUsernames(String... participants) {
        List<Conversation> conversations =
                conversationRepository.getConversationByParticipantsUsername(Arrays.asList(participants), (long) participants.length);

        switch (conversations.size()){
            case 0: return createConversation(participants);
            case 1: return conversations.get(0);
            default: throw new InternalErrorException("Internal error: there is more than one conversation by given participant names: " + Arrays.toString(participants));
        }

    }

    private Conversation createConversation(String... participantsUsernames) {
        Set<ChatUser> participants = chatUserService.findChatUserByUsernames(participantsUsernames);
        Conversation conversation = new Conversation();
        conversation.setStartedTs(Instant.now());
        conversation.setEndTs(Instant.now());
        conversation.setType(ConversationType.CHAT);
        conversation.setVanishable(false);
        conversation.setParticipants(new HashSet<>());
        participants.forEach(conversation::addParticipant);
        return conversationRepository.save(conversation);
    }


}
