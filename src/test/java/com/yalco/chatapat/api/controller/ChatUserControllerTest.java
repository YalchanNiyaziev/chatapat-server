package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.UserConnection;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.UserConnectionOperationException;
import com.yalco.chatapat.exception.UserNotFoundException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
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
    private ChatUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public void init() {
        cleanUpConnections();
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
        chatUser.setPicture("sme-picture");
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
        chatUserTwo.setPicture("sme-picture");
        chatUserTwo.setRegistrationTs(Instant.now());
        chatUserTwo.setRole(UserRole.STANDARD_USER);

        userRepository.save(chatUser);
        userRepository.save(chatUserTwo);
    }

    @AfterEach
    public void cleanUpConnections() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("")
    public void test() {}


}