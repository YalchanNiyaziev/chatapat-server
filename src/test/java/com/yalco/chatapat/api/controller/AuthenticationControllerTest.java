package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.AuthenticationRequestDto;
import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.repository.ChatUserRepository;
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

    @BeforeEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("When user is valid, expect save")
    public void userRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        controller.registerUser(user);

        List<ChatUser> countUsers = userRepository.findAll();

        assertEquals(1, countUsers.size());
        assertEquals(user.getUsername(), countUsers.get(0).getUsername());
        assertEquals(user.getFirstName(), countUsers.get(0).getFirstName());
        assertEquals(user.getLastname(), countUsers.get(0).getLastName());
        assertEquals(user.getGender(), countUsers.get(0).getGender());
        assertEquals(user.getBirthDate(), countUsers.get(0).getBirthDate());
    }

    @Test
    @DisplayName("When username is missing, expect throws")
    public void usernameMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When password is missing, expect throws")
    public void passwordMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));
    }


    @Test
    @DisplayName("When birth date is missing, expect throws")
    public void birthDateMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When user id is given, expect throws")
    public void givenUserIdRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .id(4545L)
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalStateException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When first name is missing, expect throws")
    public void firstNameMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));


    }

    @Test
    @DisplayName("When last name is missing, expect throws")
    public void lastNameMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When gender is missing, expect throws")
    public void genderMissingRegistrationTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.registerUser(user));

    }

    @Test
    @DisplayName("When valid credentials, expect login")
    public void validCredentialsLoginTest() {
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
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
        ChatUserDto user = ChatUserDto.builder()
                .birthDate(LocalDate.now())
                .firstName("Dummy")
                .lastname("Dummiev")
                .gender(ChatUserGender.MALE)
                .username("dummyUser")
                .password("dummy_pass")
                .build();

        controller.registerUser(user);
        ResponseEntity<?> response = controller.login(new AuthenticationRequestDto("dummyUser", "zzz"));
        List<String> authorizationHeader = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        assertNull(authorizationHeader);
    }

}