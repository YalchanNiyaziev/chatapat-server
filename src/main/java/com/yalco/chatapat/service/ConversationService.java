package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.ConversationMessageDto;
import com.yalco.chatapat.dto.UserConversationDto;
import com.yalco.chatapat.entity.*;
import com.yalco.chatapat.enums.ConversationType;
import com.yalco.chatapat.enums.MessageStatus;
import com.yalco.chatapat.enums.MessageType;
import com.yalco.chatapat.exception.DoesNotExistException;
import com.yalco.chatapat.exception.InternalErrorException;
import com.yalco.chatapat.repository.ConversationMessageRepository;
import com.yalco.chatapat.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository messageRepository;
    private final ChatUserService chatUserService;

    public List<UserConversationDto> getAllUserConversationsByUsername(String username) {
        List<Conversation> foundConversation = conversationRepository.findAllByParticipantsUsername(username);
        List<UserConversationDto> conversations = new ArrayList<>();
        for (Conversation conversation : foundConversation) {
            try {
                Set<ChatUser> participants = conversation.getParticipants();

                ChatUser partner = findUserConnectionPartner(participants, username);
                if (partner == null) {
                    // TODO extract info about chat room users
                    partner = new ChatUser();
                }
                ConversationMessage lastMessage = getLastMessageByConversationId(conversation.getId());

                UserConversationDto conversationDto =
                        UserConversationDto.builder()
                                .conversationId(conversation.getId())
                                .firstName(partner.getFirstName())
                                .surName(partner.getLastName())
                                .username(partner.getUsername())
                                .chatName(partner.getChatName())
                                .imageUrl(partner.getPicture())
                                .status(partner.getStatus())
                                //TODO change default message for message types different from TEXT
                                .lastMessage(MessageType.TEXT.equals(lastMessage.getType()) ? lastMessage.getContent() : "Attachment")
                                .lastMessageTs(lastMessage.getMessageTs())
                                .lastMessageSenderUsername(lastMessage.getSender().getUsername())
                                .messageStatus(lastMessage.getStatus())
                                .build();

                conversations.add(conversationDto);

            } catch (Exception ex) {
                logger.error("Exception {} in getting user conversation information for user {} in conversation with id {} and from type {}", ex.getMessage(), username, conversation.getId(), conversation.getType());
            }
        }
        return conversations;
    }

    public List<ConversationMessageDto> getAllMessagesFromConversation(Long conversationId) {
        return messageRepository
                .findAllByConversationIdOrderByMessageTsDesc(conversationId)
                .stream()
                .map(m ->
                        ConversationMessageDto.builder()
                                .content(m.getContent())
                                .messageTs(m.getMessageTs())
                                .senderName(m.getSender() != null ? m.getSender().getUsername() : null)
                                .type(m.getType())
                                .build()
                ).collect(Collectors.toList());
    }

    public void saveUserSpecificTextMessage(ConversationMessageDto textMessage) {
        Conversation conversation = getConversationByParticipantUsernames(textMessage.getReceiverName(), textMessage.getSenderName());
        ConversationMessage message = new ConversationMessage();
        message.setContent(textMessage.getContent());
        message.setSender(chatUserService.getChatUserByUsername(textMessage.getSenderName()));
        message.setMessageTs(textMessage.getMessageTs() != null ? textMessage.getMessageTs() : Instant.now());
        message.setType(MessageType.TEXT);
        message.setStatus(MessageStatus.SEND);
        message.setConversation(conversation);

        messageRepository.save(message);
    }

    // TODO thing about this and decide, which one of this teo methods that finds conversation has better performance
    public Optional<Conversation> extractConversationByParticipantsUsername(ChatUser user, String participantUsername) {
        for (Conversation conversation : user.getConversations()) {
            for (ChatUser p : conversation.getParticipants()) {
                if (p.getUsername().equals(participantUsername)) {
                    return Optional.of(conversation);
                }
            }
        }
        return Optional.empty();
    }

    public Conversation getConversationByParticipantUsernames(String... participants) {
        List<Conversation> conversations =
                conversationRepository.getConversationByParticipantsUsername(Arrays.asList(participants), (long) participants.length);

        switch (conversations.size()) {
            case 0:
                return createConversation(participants);
            case 1:
                return conversations.get(0);
            default:
                throw new InternalErrorException("Internal error: there is more than one conversation by given participant names: " + Arrays.toString(participants));
        }

    }

    public ConversationMessage getLastMessageByConversationId(Long conversationId) {
        return messageRepository.findTopByConversationIdOrderByMessageTsDesc(conversationId)
                .orElseThrow(() -> new DoesNotExistException("Unable to find conversation message by given conversation id " + conversationId));
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

    private ChatUser findUserConnectionPartner(Set<ChatUser> participants, String currentUser) {
        if (participants != null) {
            List<ChatUser> partners = participants.stream()
                    .filter(p -> !p.getUsername().equals(currentUser))
                    .collect(Collectors.toList());
            if (partners.size() == 1) {
                return partners.get(0);
            }
            // TODO IF there is more than one partner that is chat group/room
        }
        return null;

    }

//    private ChatUser findUserConnectionPartner(UserConnection connection, String username) {
//        if (connection != null && connection.getBearer() != null && connection.getRequester() != null) {
//            if (Objects.equals(username, connection.getBearer().getUsername())) {
//                return connection.getRequester();
//            }
//            if (Objects.equals(username, connection.getRequester().getUsername())) {
//                return connection.getBearer();
//            }
//        }
//        throw new UserConnectionOperationException("Not found partner for user with username " + username + " in this connection");
//    }


}
