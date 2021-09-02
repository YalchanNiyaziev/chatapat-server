package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.AuthenticationRequestDto;
import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.dto.ChatUserRegistrationRequest;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.ConversationMessageRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private ConversationMessageRepository messageRepository;

    @Autowired
    private UserConnectionRepository connectionRepository;

    @BeforeEach
    public void cleanUp() {
        connectionRepository.deleteAll();
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("When user is valid, expect save")
    public void userRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        controller.registerUser(user);

        List<ChatUser> countUsers = userRepository.findAll();

        assertEquals(1, countUsers.size());
        assertEquals(user.getEmail(), countUsers.get(0).getUsername());
        assertEquals(user.getFirstName(), countUsers.get(0).getFirstName());
        assertEquals(user.getLastName(), countUsers.get(0).getLastName());
        assertEquals(user.getGender(), countUsers.get(0).getGender());
        assertEquals(user.getBirthDate(), countUsers.get(0).getBirthDate());
    }

    @Test
    @DisplayName("When username is missing, expect throws")
    public void usernameMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When password is missing, expect throws")
    public void passwordMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));
    }

    @Test
    @DisplayName("When password confirmation is missing, expect throws")
    public void passwordConfirmationMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));
    }


    @Test
    @DisplayName("When birth date is missing, expect throws")
    public void birthDateMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When password and confirm password not match, expect throws")
    public void passwordConfirmMismatchTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("SOMETHING DIFFERENT")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When first name is missing, expect throws")
    public void firstNameMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));


    }

    @Test
    @DisplayName("When last name is missing, expect throws")
    public void lastNameMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When gender is missing, expect throws")
    public void genderMissingRegistrationTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When valid credentials, expect login")
    public void validCredentialsLoginTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        controller.registerUser(user);
        ResponseEntity<?> response = controller.login(new AuthenticationRequestDto("dummyUser", "dummy_pass"));
        List<String> authorizationHeader = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        assertNotNull(authorizationHeader);
        assertEquals(1, authorizationHeader.size());
        assertTrue(authorizationHeader.get(0).startsWith("Bearer "));
    }

    @Test
    @DisplayName("When invalid credentials, expect throws")
    public void invalidCredentialsLoginTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        controller.registerUser(user);
        ResponseEntity<?> response = controller.login(new AuthenticationRequestDto("dummyUser", "zzz"));
        List<String> authorizationHeader = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        assertNull(authorizationHeader);
    }

    @Test
    @DisplayName("When username is not unique, expect throws")
    public void usernameAlreadyInUseTest() {
        ChatUserRegistrationRequest user = ChatUserRegistrationRequest.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastName("Dummiev")
                .gender(ChatUserGender.MALE)
                .email("dummyUser")
                .password("dummy_pass")
                .passwordConfirm("dummy_pass")
                .build();

        controller.registerUser(user);
        assertThrows(IllegalArgumentException.class, ()-> controller.registerUser(user));
    }

}