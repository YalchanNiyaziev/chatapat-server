package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.UserConnectionDto;
import com.yalco.chatapat.dto.UserPendingConnectionDto;
import com.yalco.chatapat.entity.BaseEntity;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.Conversation;
import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.enums.MessageType;
import com.yalco.chatapat.exception.UserConnectionOperationException;
import com.yalco.chatapat.repository.UserConnectionRepository;
import com.yalco.chatapat.utils.ObjectConverter;
import com.yalco.chatapat.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserConnectionService {

    //TODO !!!! add check whether current action is done by current Principal


    private static final Logger logger = LoggerFactory.getLogger(UserConnectionService.class);

    private final ChatUserService userService;
    private final UserConnectionRepository connectionRepository;

    public List<UserConnectionDto> getUserSpecificUserConnections(String username) {
        List<UserConnectionDto> resultList = new ArrayList<>();
        List<UserConnection> foundConnections =
                connectionRepository.getAllConnectedUserConnectionsByUsername(username);
        for (UserConnection connection : foundConnections) {
            if (connection.getRequester() == null || connection.getBearer() == null) {
                logger.error("Invalid participants in connection with id {}", connection.getId());
                continue;
            }
            UserConnectionDto connectionDto = new UserConnectionDto();
            connectionDto.setId(connection.getId());
            connectionDto.setBlocked(false);
            connectionDto.setConnectionRequested(false);
            connectionDto.setConnected(true);
            connectionDto.setUpdatedBy(connection.getUpdatedBy());
            ChatUser partner = connection.getRequester();
            if (Objects.equals(partner.getUsername(), username)) {
                partner = connection.getBearer();
            }
            ChatUserDto partnerDto = ObjectConverter.convertObject(partner, ChatUserDto.class);
            connectionDto.setPartner(partnerDto);

            resultList.add(connectionDto);
        }
        return resultList;
    }

    public List<UserConnectionDto> getAllBlockedConnections(String username) {
        List<UserConnectionDto> resultList = new ArrayList<>();
        List<UserConnection> foundBlocked = connectionRepository.getAllBlockedConnectionsByUsername(username);
        for (UserConnection blockedConnection : foundBlocked) {
            if (blockedConnection.getRequester() == null || blockedConnection.getBearer() == null) {
                logger.error("Invalid participants in connection with id {}", blockedConnection.getId());
                continue;
            }
            UserConnectionDto connectionDto = new UserConnectionDto();
            connectionDto.setId(blockedConnection.getId());
            connectionDto.setBlocked(true);
            connectionDto.setConnectionRequested(false);
            connectionDto.setConnected(false);
            connectionDto.setUpdatedBy(blockedConnection.getUpdatedBy());
            ChatUser partner = blockedConnection.getRequester();
            if (Objects.equals(partner.getUsername(), username)) {
                partner = blockedConnection.getBearer();
            }
            ChatUserDto partnerDto = ObjectConverter.convertObject(partner, ChatUserDto.class);
            connectionDto.setPartner(partnerDto);

            resultList.add(connectionDto);
        }
        return resultList;
    }

    public boolean isBlockedConnection(String participantOne, String participantTwo) {
        return connectionRepository.existBlockedUserConnection(participantOne, participantTwo);
    }

    public boolean isConnectedConnection(String participantOne, String participantTwo) {
        return connectionRepository.existConnectedUserConnection(participantOne, participantTwo);
    }


    public void removeUserConnection(String username, String removedUsername) {
        UserConnection foundConnection = connectionRepository.findUserConnectionByParticipants(username, removedUsername)
                .orElseThrow(() -> new UserConnectionOperationException("There is no connection with " + username + "and " + removedUsername));

        if (!ServiceUtils.isConnected(foundConnection)) {
            throw new UserConnectionOperationException("Can not remove unconnected connection. To remove connection it must be in connected state");
        }

        foundConnection.setConnected(false);
        foundConnection.setConnectionRequest(false);
        foundConnection.setBlocked(false);
        foundConnection.setLastUpdateTs(Instant.now());
        foundConnection.setUpdatedBy(username);

        connectionRepository.saveAndFlush(foundConnection);
    }

    public void blockUserConnection(String requester, String blockedUsername) {
        Optional<UserConnection> foundConnection = connectionRepository.findUserConnectionByParticipants(requester, blockedUsername);
        UserConnection connection = null;

        if (!foundConnection.isPresent()) {
            connection = createInitialConnection(requester, blockedUsername);
            connection.setBlocked(true);
        } else {
            connection = foundConnection.get();
            connection.setConnected(false);
            connection.setConnectionRequest(false);
            connection.setBlocked(true);
            connection.setLastUpdateTs(Instant.now());
            connection.setUpdatedBy(requester);
        }
        connectionRepository.saveAndFlush(connection);
    }

    public void unblockUserConnection(String requester, String unblockedUsername) {
        // TODO try to return unblocked connection it's previous state
        UserConnection foundConnection = connectionRepository.findUserConnectionByParticipants(requester, unblockedUsername)
                .orElseThrow(() -> new UserConnectionOperationException("There is no connection with " + requester + "and " + unblockedUsername));
        if (!ServiceUtils.isBlocked(foundConnection)) {
            throw new UserConnectionOperationException("Can not unblock not blocked connection. To unblock connection it must be in blocked state");
        }
        foundConnection.setConnected(false);
        foundConnection.setConnectionRequest(false);
        foundConnection.setBlocked(false);
        foundConnection.setLastUpdateTs(Instant.now());
        foundConnection.setUpdatedBy(requester);

        connectionRepository.saveAndFlush(foundConnection);
    }

    public List<UserConnectionDto> getAllPendingConnectionRequest(String username) {
        List<UserConnectionDto> pendingConnections = new ArrayList<>();
        List<UserConnection> foundConnections = connectionRepository.getAllPendingConnectionRequestByUsername(username);

        for (UserConnection connection : foundConnections) {
            if (connection.getRequester() == null || connection.getBearer() == null) {
                logger.error("Invalid participants in connection with id {}", connection.getId());
                continue;
            }
            UserConnectionDto connectionDto = new UserConnectionDto();
            connectionDto.setId(connection.getId());
            connectionDto.setBlocked(false);
            connectionDto.setConnectionRequested(true);
            connectionDto.setConnected(false);
            connectionDto.setUpdatedBy(connection.getUpdatedBy());
            ChatUser partner = connection.getRequester();
            if (Objects.equals(partner.getUsername(), username)) {
                partner = connection.getBearer();
            }
            ChatUserDto partnerDto = ObjectConverter.convertObject(partner, ChatUserDto.class);
            connectionDto.setPartner(partnerDto);

            pendingConnections.add(connectionDto);
        }

        return pendingConnections;
    }

    public void sendConnectionRequest(String senderName, String receiverName) {

        UserConnection connectionRequest = null;
        Optional<UserConnection> existingConnection = connectionRepository.findUserConnectionByParticipants(senderName, receiverName);
        if (existingConnection.isPresent()) {
            connectionRequest = existingConnection.get();
            if (!ServiceUtils.isRemoved(connectionRequest)) {
                throw new UserConnectionOperationException("Can not create user connection request. Existing connection must be in removed state.");
            }
            connectionRequest.setConnected(false);
            connectionRequest.setConnectionRequest(true);
            connectionRequest.setBlocked(false);
            connectionRequest.setLastUpdateTs(Instant.now());
            connectionRequest.setUpdatedBy(senderName);
        } else {
            connectionRequest = createInitialConnection(senderName, receiverName);
            connectionRequest.setConnectionRequest(true);
        }

        connectionRepository.save(connectionRequest);
    }

    public void acceptConnectionRequest(String reviewer, String acceptedUsername) {
        UserConnection connectionRequest =
                connectionRepository.findPendingConnectionRequestByParticipants(reviewer, acceptedUsername)
                        .orElseThrow(() -> new UserConnectionOperationException("There is no pending connection request to " + reviewer + " from " + acceptedUsername));

        connectionRequest.setConnected(true);
        connectionRequest.setConnectionRequest(false);
        connectionRequest.setLastUpdateTs(Instant.now());
        connectionRequest.setUpdatedBy(reviewer);

        connectionRepository.saveAndFlush(connectionRequest);
    }

    public void rejectConnectionRequest(String reviewer, String rejectedUsername) {
        UserConnection connectionRequest =
                connectionRepository.findPendingConnectionRequestByParticipants(reviewer, rejectedUsername)
                        .orElseThrow(() -> new UserConnectionOperationException("There is not pending connection request to " + reviewer + " from " + rejectedUsername));

        connectionRepository.delete(connectionRequest);
    }

    private UserConnection createInitialConnection(String requester, String bearer) {
        ChatUser sender = userService.getChatUserByUsername(requester);
        ChatUser receiver = userService.getChatUserByUsername(bearer);

        if (Objects.equals(sender.getUsername(), receiver.getUsername())) {
            throw new UserConnectionOperationException("Can not create user connection request, requester and receiver has same usernames");
        }
        UserConnection userConnection = new UserConnection();
        userConnection.setRequester(sender);
        userConnection.setBearer(receiver);
        userConnection.setConnected(false);
        userConnection.setBlocked(false);
        userConnection.setConnectionRequest(false);
        userConnection.setLastUpdateTs(Instant.now());
        userConnection.setUpdatedBy(requester);
        return userConnection;
    }

}
