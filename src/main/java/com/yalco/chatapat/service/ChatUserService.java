package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.SearchChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.UserNotFoundException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.utils.ObjectConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private static final Logger logger = LoggerFactory.getLogger(ChatUserService.class);

    private final ChatUserRepository chatUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ChatUserDto> getALlChatUsers() {
        List<ChatUser> chatUsers = chatUserRepository.findAll();
        return ObjectConverter.convertList(chatUsers, ChatUserDto.class);
    }

    public List<ChatUserDto> searchChatUser(SearchChatUserDto search) {
        return null;
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

    public ChatUser findChatUserByUsername(String username) {
        return chatUserRepository.findChatUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist."));
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


}
