package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.SearchChatUserDto;
import com.yalco.chatapat.dto.UserPendingConnectionDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.UserConnectionOperationException;
import com.yalco.chatapat.exception.UserNotFoundException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
import com.yalco.chatapat.utils.ObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;

@Service
public class ChatUserService {
    //TODO !!!! add check whether current action is done by current Principal

    private static final Logger logger = LoggerFactory.getLogger(ChatUserService.class);

    private final ChatUserRepository chatUserRepository;
    private final UserConnectionRepository connectionRepository;
    private final PasswordEncoder passwordEncoder;

    public ChatUserService(ChatUserRepository chatUserRepository, UserConnectionRepository connectionRepository, PasswordEncoder passwordEncoder) {
        this.chatUserRepository = chatUserRepository;
        this.connectionRepository = connectionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ChatUserDto> getALlChatUsers() {
        List<ChatUser> chatUsers = chatUserRepository.findAll();
        return ObjectConverter.convertList(chatUsers, ChatUserDto.class);
    }

    public List<ChatUserDto> searchChatUser(SearchChatUserDto search) {
        return null;
    }


    public void removeUserConnection(String username, String removedUsername) {
        UserConnection foundConnection = connectionRepository.findUserConnectionByParticipants(username, removedUsername)
                .orElseThrow(() -> new UserConnectionOperationException("There is no connection with " + username + "and " + removedUsername));

        if (!isConnected(foundConnection)) {
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
        UserConnection foundConnection = connectionRepository.findUserConnectionByParticipants(requester, unblockedUsername)
                .orElseThrow(() -> new UserConnectionOperationException("There is no connection with " + requester + "and " + unblockedUsername));
        if (!isBlocked(foundConnection)) {
            throw new UserConnectionOperationException("Can not unblock not blocked connection. To unblock connection it must be in blocked state");
        }
        foundConnection.setConnected(false);
        foundConnection.setConnectionRequest(false);
        foundConnection.setBlocked(false);
        foundConnection.setLastUpdateTs(Instant.now());
        foundConnection.setUpdatedBy(requester);

        connectionRepository.saveAndFlush(foundConnection);
    }

    public List<UserPendingConnectionDto> getAllPendingConnectionRequest(String username) {
        List<UserPendingConnectionDto> pendingConnection = new ArrayList<>();
        List<UserConnection> foundConnections = connectionRepository.getAllPendingConnectionRequestByUsername(username);
        foundConnections.forEach(c ->
                pendingConnection.add(
                        new UserPendingConnectionDto(ObjectConverter.convertObject(c.getRequester(), ChatUserDto.class), c.getLastUpdateTs())
                )
        );
        return pendingConnection;
    }

    public void sendConnectionRequest(String senderName, String receiverName) {

        UserConnection connectionRequest = null;
        Optional<UserConnection> existingConnection = connectionRepository.findUserConnectionByParticipants(senderName, receiverName);
        if (existingConnection.isPresent()) {
            connectionRequest = existingConnection.get();
            if (!isRemoved(connectionRequest)) {
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

    public void registerChatUser(ChatUserDto user) {
        validateUser(user);
        ChatUser chatUser = ObjectConverter.convertObject(user, ChatUser.class);
        chatUser.setPassword(passwordEncoder.encode(chatUser.getPassword()));
        chatUser.setClosed(false);
        chatUser.setLocked(false);
        chatUser.setRegistrationTs(Instant.now());
        chatUser.setStatus(ChatUserStatus.ACTIVE);
        chatUser.setRole(UserRole.STANDARD_USER);
        chatUser = chatUserRepository.save(chatUser);
        logger.trace("User with username {} was created at {}", chatUser.getUsername(), chatUser.getRegistrationTs());
    }

    public Set<ChatUser> findChatUserByUsernames(String... usernames) {
        Set<ChatUser> foundUsers = chatUserRepository.findAllByUsernameIn(Arrays.asList(usernames));
        return foundUsers;
    }

    public ChatUser findChatUserByUsername(String username){
        return chatUserRepository.findChatUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " does not exist."));
    }

    private void validateUser(ChatUserDto user) {
        Assert.notNull(user, "User must be provided");

        if (user.getId() != null) {
            throw new IllegalStateException("User id can not be given as parameter in user creation process");
        }
        if (user.getClosed() != null) {
            throw new IllegalStateException("User close status can not be given as parameter in user creation process");
        }
        if (user.getLocked() != null) {
            throw new IllegalStateException("User lock status can not be given as parameter in user creation process");
        }
        if (user.getStatus() != null) {
            throw new IllegalStateException("User active status can not be given as parameter in user creation process");
        }
        Assert.notNull(user.getUsername(), "Username must be provided");
        Assert.hasLength(user.getUsername(), "Username must not be empty");
        Optional<ChatUser> foundUser = chatUserRepository.findChatUserByUsername(user.getUsername());
        Assert.isNull(foundUser.orElse(null), "Username already in use");

        Assert.notNull(user.getPassword(), "Password must be provided");
        Assert.hasLength(user.getPassword(), "Password must not be empty");

        Assert.notNull(user.getBirthDate(), "Birth date must be provided");

        Assert.notNull(user.getFirstName(), "First name must be provided");
        Assert.hasLength(user.getFirstName(), "First name must not be empty");

        Assert.notNull(user.getLastname(), "Last name must be provided");
        Assert.hasLength(user.getLastname(), "Last name must not be empty");

        Assert.notNull(user.getGender(), "Gender must be provided");
    }

    private ChatUser getChatUserByUsername(String username) {
        Assert.notNull(username, "Username must be provided");
        Assert.hasLength(username, "Username must not be empty");
        Optional<ChatUser> foundUser = chatUserRepository.findChatUserByUsername(username);
        return foundUser.orElseThrow(() -> new UserNotFoundException("Not found user with username " + username));
    }

    private UserConnection createInitialConnection(String requester, String bearer) {
        ChatUser sender = getChatUserByUsername(requester);
        ChatUser receiver = getChatUserByUsername(bearer);

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

    private boolean isConnectionRequested(UserConnection connection) {
        return connection.isConnectionRequest() && !connection.isConnected() && !connection.isBlocked();
    }

    private boolean isConnected(UserConnection connection) {
        return connection.isConnected() && !connection.isBlocked() && !connection.isConnectionRequest();
    }

    private boolean isRemoved(UserConnection connection) {
        return !connection.isConnectionRequest() && !connection.isConnected() && !connection.isBlocked();
    }

    private boolean isBlocked(UserConnection connection) {
        return connection.isBlocked() && !connection.isConnected() && !connection.isConnectionRequest();
    }
}
