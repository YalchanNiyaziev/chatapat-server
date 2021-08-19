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
class UserConnectionControllerTest {

    @Autowired
    private UserConnectionController connectionController;

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
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertEquals(1, connections.size());
        assertEquals("fake1", connections.get(0).getRequester().getUsername());
        assertEquals("fake2", connections.get(0).getBearer().getUsername());
        assertTrue(connections.get(0).isConnectionRequest());
        assertFalse(connections.get(0).isConnected());
        assertFalse(connections.get(0).isBlocked());
        assertEquals("fake1", connections.get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When requester and receiver has same usernames, expect throws")
    public void sendConnectionRequestWithSameUsernamesTest() {
        assertThrows(UserConnectionOperationException.class, () ->
                connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake1").build()));
    }

    @Test
    @DisplayName("When un existing usernames, expect throws")
    public void sendConnectionRequestWithUnExistingUsersTest() {
        assertThrows(UserNotFoundException.class, () -> connectionController.sendConnectionRequest("kkkk", ChatUserDto.builder().username("aaaaa").build()));
    }

    @Test
    @DisplayName("When user connection request exists and it is not in removed state, expect throws")
    public void sendConnectionRequestToExistingConnectionTest() {
        createUserConnection();
        assertThrows(UserConnectionOperationException.class, () ->
                connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build()));
    }

    @Test
    @DisplayName("When accept valid user connection request, expect update to real connection")
    public void acceptValidConnectionRequestTest() {
        createUserConnection();

        List<UserConnection> connections = connectionRepository.findAll();
        assertEquals(1, connections.size());
        assertEquals("fake1", connections.get(0).getRequester().getUsername());
        assertEquals("fake2", connections.get(0).getBearer().getUsername());
        assertFalse(connections.get(0).isConnectionRequest());
        assertTrue(connections.get(0).isConnected());
        assertFalse(connections.get(0).isBlocked());
        assertEquals("fake2", connections.get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When given same users as participant in user connection request, expect throws")
    public void acceptConnectionRequestWithSameUsersTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.acceptConnectionRequest("fake2", "fake2"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When connection request accepter name is same as requester, expect trows")
    public void acceptWithRequesterUserTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.acceptConnectionRequest("fake1", "fake1"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When requester and accepter have changed their position , expect trows")
    public void acceptWithChangedRequesterAndAccepterUserTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.acceptConnectionRequest("fake1", "fake2"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When given not existing user as participants in user connection request, expect throws")
    public void acceptConnectionRequestWithUnExistingUsersTest() {
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.acceptConnectionRequest("fassske1", "fadddke1"));
        assertEquals(0, connections.size());
    }

    @Test
    @DisplayName("When reject valid users as participant in user connection request, expect delete")
    public void rejectValidConnectionRequestTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        connectionController.rejectConnectionRequest("fake2", "fake1");
        List<UserConnection> connections = connectionRepository.findAll();
        assertEquals(0, connections.size());
        assertFalse(connectionRepository.existUserConnection("fake1", "fake2"));
    }

    @Test
    @DisplayName("When reject same users as participant in user connection request, expect throws")
    public void rejectConnectionRequestWithSameUsersTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.rejectConnectionRequest("fake1", "fake1"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When reject not existing user as participants in user conversation request, expect throws")
    public void rejectConnectionRequestWithUnExistingUsersTest() {
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.rejectConnectionRequest("fakddde1", "ddd"));
        assertEquals(0, connections.size());
    }

    @Test
    @DisplayName("When reject connection request reviewer name is same as requester, expect trows")
    public void rejectWithRequesterUserTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.rejectConnectionRequest("fake1", "fake1"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When requester and reject reviewer have changed their position, expect trows")
    public void rejectWithChangedRequesterAndAccepterUserTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        List<UserConnection> connections = connectionRepository.findAll();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.rejectConnectionRequest("fake1", "fake2"));
        assertEquals(1, connections.size());
    }

    @Test
    @DisplayName("When remove user connection with valid participant names, expect all flags false")
    public void removeUserConnectionTest() {
        createUserConnection();

        assertEquals(1, connectionRepository.findAll().size());

        connectionController.removeConnection("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertFalse(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When remove blocked user connection, expect throws")
    public void removeBlockedConnectionTest() {
        createBlockedUserConnection();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.removeConnection("fake1", "fake2"));
    }

    @Test
    @DisplayName("When remove connection requested user connection, expect throws")
    public void removeConnectionRequestedConnectionTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        assertThrows(UserConnectionOperationException.class, () -> connectionController.removeConnection("fake1", "fake2"));
    }

    @Test
    @DisplayName("When remove user connection with invalid participants names, expect throw")
    public void removeUserConnectionInvalidParticipantsTest() {
        createUserConnection();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.removeConnection("faksse1", "fakdde2"));
    }

    @Test
    @DisplayName("When block user connection with valid participants name, expects update")
    public void blockUserConnectionTest() {
        createBlockedUserConnection();

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertTrue(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When block removed user connection, expect update connection")
    public void blockRemovedConnectionTest() {
        createRemovedConnection();
        connectionController.blockUser("fake1", "fake2");
        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertTrue(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When block connection requested user connection, expect update connection")
    public void blockConnectionRequestedConnectionTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        connectionController.blockUser("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertTrue(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When block user connection with invalid participants name, expects throws")
    public void blockUserConnectionInvalidParticipantsNamesTest() {
        createUserConnection();
        assertThrows(UserNotFoundException.class, () -> connectionController.blockUser("faksse1", "fakdde2"));
    }

    @Test
    @DisplayName("When block not existing user connection, expect create new connection and block")
    public void blockNotExistingConnection() {
        connectionController.blockUser("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertTrue(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    @Test
    @DisplayName("When unblock user connection with valid participants name, expects update")
    public void unblockUserConnectionTest() {
        createBlockedUserConnection();

        connectionController.unblockUser("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertFalse(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());

    }

    @Test
    @DisplayName("When unblock user connection with invalid participants name, expects throws")
    public void unblockUserConnectionInvalidParticipantsNamesTest() {
        createBlockedUserConnection();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.unblockUser("faksse1", "fakdde2"));
    }

    @Test
    @DisplayName("When unblock user connection in removed state, expects throws")
    public void unblockRemovedUserConnectionTest() {
        createRemovedConnection();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.unblockUser("fake1", "fake2"));
    }

    @Test
    @DisplayName("When unblock user connection in connected state, expects throws")
    public void unblockConnectedUserConnectionTest() {
        createUserConnection();
        assertThrows(UserConnectionOperationException.class, () -> connectionController.unblockUser("fake1", "fake2"));
    }

    @Test
    @DisplayName("When unblock user connection in connection requested state, expects throws")
    public void unblockConnectionRequestedUserConnectionTest() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        assertThrows(UserConnectionOperationException.class, () -> connectionController.unblockUser("fake1", "fake2"));
    }


    private void createUserConnection() {
        connectionController.sendConnectionRequest("fake1", ChatUserDto.builder().username("fake2").build());
        connectionController.acceptConnectionRequest("fake2", "fake1");
    }

    private void createBlockedUserConnection() {
        createUserConnection();

        assertEquals(1, connectionRepository.findAll().size());

        connectionController.blockUser("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertTrue(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());
    }

    private void createRemovedConnection() {
        createUserConnection();
        assertEquals(1, connectionRepository.findAll().size());
        connectionController.removeConnection("fake1", "fake2");

        assertEquals(1, connectionRepository.findAll().size());
        assertFalse(connectionRepository.findAll().get(0).isConnectionRequest());
        assertFalse(connectionRepository.findAll().get(0).isConnected());
        assertFalse(connectionRepository.findAll().get(0).isBlocked());
        assertEquals("fake1", connectionRepository.findAll().get(0).getUpdatedBy());

    }

}