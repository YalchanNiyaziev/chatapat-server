package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.UserConnectionCreationException;
import com.yalco.chatapat.exception.UserNotFoundException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
import com.yalco.chatapat.utils.ObjectConverter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatUserControllerTest {

    @Autowired
    private ChatUserController chatUserController;

    @Autowired
    private UserConnectionRepository connectionRepository;

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public void init() {
        connectionRepository.deleteAll();
        userRepository.deleteAll();

        ChatUser chatUser = new ChatUser();
        chatUser.setUsername("fake1");
        chatUser.setPassword(passwordEncoder.encode("fake_pass1"));
        chatUser.setFirstName("Fake");
        chatUser.setLastName("One");
        chatUser.setBirthDate(LocalDate.now());
        chatUser.setGender(ChatUserGender.UNKNOWN);
        chatUser.setStatus(ChatUserStatus.ACTIVE);
        chatUser.setClosed(false);
        chatUser.setLocked(false);
        chatUser.setRegistrationTs(Instant.now());
        chatUser.setRole(UserRole.STANDARD_USER);

        ChatUser chatUserTwo = new ChatUser();
        chatUserTwo.setUsername("fake2");
        chatUserTwo.setPassword(passwordEncoder.encode("fake_pass2"));
        chatUserTwo.setFirstName("Fake");
        chatUserTwo.setLastName("Two");
        chatUserTwo.setBirthDate(LocalDate.now());
        chatUserTwo.setGender(ChatUserGender.UNKNOWN);
        chatUserTwo.setStatus(ChatUserStatus.ACTIVE);
        chatUserTwo.setClosed(false);
        chatUserTwo.setLocked(false);
        chatUserTwo.setRegistrationTs(Instant.now());
        chatUserTwo.setRole(UserRole.STANDARD_USER);

        userRepository.save(chatUser);
        userRepository.save(chatUserTwo);
    }

    @AfterEach
    public void cleanUpConnections() {
        connectionRepository.deleteAll();
    }


    @Test
    @DisplayName("When valid users, expect save")
    public void sendConnectionRequestTest() {
        chatUserController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertEquals(1, connections.size());
        assertEquals("fake1", connections.get(0).getRequester().getUsername());
        assertEquals("fake2", connections.get(0).getBearer().getUsername());
        assertTrue( connections.get(0).isConnectionRequest());
        assertFalse(connections.get(0).isConnected());
        assertFalse(connections.get(0).isBlocked());
        assertEquals("fake1", connections.get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When requester and receiver has same usernames, expect throws")
    public void sendConnectionRequestWithSameUsernamesTest() {
        assertThrows(UserConnectionCreationException.class, () ->
                chatUserController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake1").build()));
    }

    @Test
    @DisplayName("When un existing usernames, expect throws")
    public void sendConnectionRequestWithUnExistingUsersTest() {
        assertThrows(UserNotFoundException.class, () -> chatUserController.sendConnectionRequest("kkkk", ChatUserDto.builder().username("aaaaa").build()));
    }

    @Test
    @DisplayName("When user connection request exists, expect throws")
    public void sendDuplicateConnectionRequestTest() {
        chatUserController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        assertThrows(UserConnectionCreationException.class, () ->
                chatUserController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build()));

    }
}