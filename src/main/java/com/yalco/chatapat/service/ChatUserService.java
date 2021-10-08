package com.yalco.chatapat.service;

import com.yalco.chatapat.dto.AddressDto;
import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.ChatUserRegistrationRequest;
import com.yalco.chatapat.dto.SearchChatUserDto;
import com.yalco.chatapat.entity.Address;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.UserConnectionOperationException;
import com.yalco.chatapat.exception.UserNotFoundException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
import com.yalco.chatapat.repository.specification.ChatUserSpecification;
import com.yalco.chatapat.utils.ObjectConverter;
import com.yalco.chatapat.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private static final Logger logger = LoggerFactory.getLogger(ChatUserService.class);

    @Value("${default.picture.user}")
    private String defaultProfileImage;

    private final ChatUserRepository chatUserRepository;
    private final UserConnectionRepository connectionRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ChatUserDto> getALlChatUsers() {
        List<ChatUser> chatUsers = chatUserRepository.findAll();
        return ObjectConverter.convertList(chatUsers, ChatUserDto.class);
    }

    public ChatUserDto updateUserInfo(String username, ChatUserDto updatedUserInfo) {
        validateUpdateUserData(updatedUserInfo);
        ChatUser userToUpdate = getChatUserByUsername(username);
        ServiceUtils.checkSelfOperation(username);

        userToUpdate.setChatName(updatedUserInfo.getChatName());
        userToUpdate.setFirstName(updatedUserInfo.getFirstName());
        userToUpdate.setLastName(updatedUserInfo.getLastName());
        userToUpdate.setBirthDate(updatedUserInfo.getBirthDate());
        userToUpdate.setGender(updatedUserInfo.getGender());
        if (updatedUserInfo.getAddress() != null) {
            if (userToUpdate.getAddress() == null) {
                userToUpdate.setAddress(new Address());
            }
            AddressDto addressDto = updatedUserInfo.getAddress();
            userToUpdate.getAddress().setCountry(addressDto.getCountry());
            userToUpdate.getAddress().setCity(addressDto.getCity());
            userToUpdate.getAddress().setStreet(addressDto.getStreet());
            userToUpdate.getAddress().setPostCode(addressDto.getPostCode());
        } else {
            userToUpdate.setAddress(null);
        }
        return ObjectConverter.convertObject(
                chatUserRepository.save(userToUpdate), ChatUserDto.class
        );
    }

    private void validateUpdateUserData(ChatUserDto updatedUserInfo) {
        Assert.notNull(updatedUserInfo, "Not null updated user info is required");
        Assert.hasText(updatedUserInfo.getFirstName(), "Valid user first name is required");
        Assert.hasText(updatedUserInfo.getLastName(), "Valid user last name is required");
        Assert.notNull(updatedUserInfo.getBirthDate(), "Valid user birth date is required");
        Assert.notNull(updatedUserInfo.getGender(), "Valid user gender is required");
        if(updatedUserInfo.getAddress() != null) {
            AddressDto addressDto = updatedUserInfo.getAddress();
            Assert.hasText(addressDto.getCountry(), "Valid country is required");
            Assert.hasText(addressDto.getCity(), "Valid city is required");
            Assert.hasText(addressDto.getStreet(), "Valid street is required");
            Assert.hasText(addressDto.getPostCode(), "Valid post code is required");
        }
    }

    public List<ChatUserDto> searchChatUserByStandardUser(SearchChatUserDto search) {
        List<ChatUserDto> resultList = new ArrayList<>();
        if (!StringUtils.hasText(search.getUsername()) && !StringUtils.hasText(search.getFirstName())
                && !StringUtils.hasText(search.getLastName()) && !StringUtils.hasText(search.getChatName())
                && search.getAddress() != null && !StringUtils.hasText(search.getAddress().getCountry())
                && !StringUtils.hasText(search.getAddress().getCity())) {
            return resultList;
        }
        String currentUser = ServiceUtils.getAuthenticatedUsername();
        if (currentUser == null) {
            return resultList;
        }
        search.setRole(UserRole.STANDARD_USER);
        search.setStatus(null);
        search.setLocked(null);
        search.setClosed(null);
        search.setRegisterBeforeTs(null);
        search.setRegisterAfterTs(null);

        // TODO make it pageable
        List<ChatUser> foundUsers = chatUserRepository.findAll(new ChatUserSpecification(search));
        for (ChatUser user : foundUsers) {
            UserConnection foundConnection = connectionRepository.findUserConnectionByParticipants(currentUser, user.getUsername())
                    .orElse(null);
            if (foundConnection != null && ServiceUtils.isBlocked(foundConnection)) {
                // DO Not pass blocked connections in search results
                continue;
            }
            ChatUserDto dto = ObjectConverter.convertObject(user, ChatUserDto.class);
            dto.setClosed(null);
            dto.setLocked(null);
            dto.setRole(null);
            dto.setSelf(Objects.equals(currentUser, dto.getUsername()));
            dto.setConnected(foundConnection != null && ServiceUtils.isConnected(foundConnection));
            dto.setPending(foundConnection != null && ServiceUtils.isConnectionRequested(foundConnection));
            dto.setIsSenderConnectionRequest(foundConnection != null && Objects.equals(foundConnection.getUpdatedBy(), currentUser));
            resultList.add(dto);
        }
        return resultList;
    }

    public void registerChatUser(ChatUserRegistrationRequest user) {
        validateUser(user);

        ChatUser chatUser = new ChatUser();
        chatUser.setFirstName(user.getFirstName());
        chatUser.setLastName(user.getLastName());
        chatUser.setBirthDate(user.getBirthDate());
        chatUser.setGender(user.getGender());
        chatUser.setUsername(user.getEmail());
        chatUser.setPassword(passwordEncoder.encode(user.getPassword()));
        chatUser.setClosed(false);
        chatUser.setLocked(false);
        chatUser.setRegistrationTs(Instant.now());
        chatUser.setStatus(ChatUserStatus.ACTIVE);
        chatUser.setRole(UserRole.STANDARD_USER);
        chatUser.setPicture(defaultProfileImage);
        chatUser = chatUserRepository.save(chatUser);
        logger.trace("User with username {} was created at {}", chatUser.getUsername(), chatUser.getRegistrationTs());
    }

    public Set<ChatUser> findChatUserByUsernames(String... usernames) {
        Set<ChatUser> foundUsers = chatUserRepository.findAllByUsernameIn(Arrays.asList(usernames));
        return foundUsers;
    }

    public ChatUserDto getChatUseDtoByUsername(String username) {
        return ObjectConverter.convertObject(getChatUserByUsername(username), ChatUserDto.class);
    }

    public ChatUser getChatUserByUsername(String username) {
        Assert.notNull(username, "Username must be provided");
        Assert.hasLength(username, "Username must not be empty");
        return findChatUserByUsername(username);
    }

    private ChatUser findChatUserByUsername(String username) {
        return chatUserRepository.findChatUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist."));
    }

    private void validateUser(ChatUserRegistrationRequest user) {
        Assert.notNull(user, "User must be provided");
        Assert.notNull(user.getEmail(), "Email must be provided");
        Assert.hasLength(user.getEmail(), "Email must not be empty");
        Optional<ChatUser> foundUser = chatUserRepository.findChatUserByUsername(user.getEmail());
        Assert.isNull(foundUser.orElse(null), "Username already in use");

        Assert.notNull(user.getPassword(), "Password must be provided");
        Assert.hasLength(user.getPassword(), "Password must not be empty");

        Assert.notNull(user.getPassword(), "Password confirmation must be provided");
        Assert.hasLength(user.getPassword(), "Password confirmation must not be empty");

        Assert.isTrue(doesPasswordConfirm(user.getPassword(), user.getPasswordConfirm()), "Password must match with confirm pass");
        Assert.notNull(user.getBirthDate(), "Birth date must be provided");

        Assert.notNull(user.getFirstName(), "First name must be provided");
        Assert.hasLength(user.getFirstName(), "First name must not be empty");

        Assert.notNull(user.getLastName(), "Last name must be provided");
        Assert.hasLength(user.getLastName(), "Last name must not be empty");

        Assert.notNull(user.getGender(), "Gender must be provided");
    }

    private boolean doesPasswordConfirm(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }

}
