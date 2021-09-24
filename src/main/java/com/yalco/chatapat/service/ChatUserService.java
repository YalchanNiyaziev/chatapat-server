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
import com.yalco.chatapat.exception.AccessDeniedException;
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

    public void updateUserInfo(String username, ChatUserDto updateInfoData) {
        if(!ServiceUtils.isAuthorizedRequester(username)) {
            throw new AccessDeniedException("Can not execute user profile update request. Given user not have authorities to make changes on this user.");
        }
        validateUserUpdateInfo(updateInfoData);
        ChatUser userToUpdate = chatUserRepository.findChatUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Not found user with username " + username));

        userToUpdate.setFirstName(updateInfoData.getFirstName());
        userToUpdate.setLastName(updateInfoData.getLastName());
        userToUpdate.setChatName(updateInfoData.getChatName());
        userToUpdate.setBirthDate(userToUpdate.getBirthDate());
        userToUpdate.setGender(updateInfoData.getGender());
        userToUpdate.setStatus(updateInfoData.getStatus());
        if(updateInfoData.getAddress() != null){
            AddressDto addressDto = updateInfoData.getAddress();
            validateAddress(addressDto);
            Address address = new Address();
            address.setCountry(addressDto.getCountry());
            address.setCity(addressDto.getCity());
            address.setStreet(addressDto.getStreet());
            address.setPostCode(addressDto.getPostCode());
            userToUpdate.setAddress(address);
        }
        chatUserRepository.save(userToUpdate);
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

        Assert.hasText(user.getEmail(), "Email must be provided");
        Optional<ChatUser> foundUser = chatUserRepository.findChatUserByUsername(user.getEmail());
        Assert.isNull(foundUser.orElse(null), "Username already in use");
        Assert.hasText(user.getPassword(), "Password must be provided");
        Assert.hasText(user.getPassword(), "Password confirmation must be provided");
        Assert.isTrue(doesPasswordConfirm(user.getPassword(), user.getPasswordConfirm()), "Password must match with confirm pass");
        Assert.notNull(user.getBirthDate(), "Birth date must be provided");
        Assert.hasText(user.getFirstName(), "First name must be provided");
        Assert.hasText(user.getLastName(), "Last name must be provided");
        Assert.notNull(user.getGender(), "Gender must be provided");
    }

    private void validateUserUpdateInfo(ChatUserDto updateInfoData) {
        Assert.notNull(updateInfoData, "User info must be provided");
        Assert.hasText(updateInfoData.getFirstName(), "Valid first name is required");
        Assert.hasText(updateInfoData.getLastName(), "Valid last name is required");
        Assert.notNull(updateInfoData.getBirthDate(), "Valid birth date is required");
        Assert.notNull(updateInfoData.getGender(), "Valid gender is required");
    }

    private void validateAddress(AddressDto address) {
        Assert.notNull(address, "Address must be provided");
        Assert.hasText(address.getCountry(), "Address country must be provided");
        Assert.hasText(address.getCity(), "Address city must be provided");
        Assert.hasText(address.getStreet(), "Address street must be provided");
        Assert.hasText(address.getPostCode(), "Address post code must be provided");
    }

    private boolean doesPasswordConfirm(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }

}
