package com.yalco.chatapat.api.controller;

import com.yalco.chatapat.dto.AddressDto;
import com.yalco.chatapat.dto.ChatUserDto;
import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import com.yalco.chatapat.exception.OperationNotAllowedException;
import com.yalco.chatapat.repository.ChatUserRepository;
import com.yalco.chatapat.repository.UserConnectionRepository;
import com.yalco.chatapat.security.JpaUserDetails;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;
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

    @BeforeEach
    public void init() {
        cleanUp();

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
        chatUser.setPicture("some-picture");

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
        chatUserTwo.setPicture("some-picture");

        UserDetails user = new JpaUserDetails(chatUser);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null,
                        ofNullable(user).map(UserDetails::getAuthorities).orElse(new ArrayList<>()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        userRepository.save(chatUser);
        userRepository.save(chatUserTwo);
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
        connectionRepository.deleteAll();
    }


    @Test
    @DisplayName("when given null username on update user info, expect throws")
    public void updateUserInfoNullUsernameThrows() {
        ChatUserDto validChatUser = ChatUserDto.builder()
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser(null, validChatUser));
    }

    @Test
    @DisplayName("when given empty username on update user info, expect throws")
    public void updateUserInfoGivenEmptyUsernameThrows() {
        ChatUserDto validChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("", validChatUser));
    }

    @Test
    @DisplayName("when null update info on update user, expect throws")
    public void updateUserNullUpdateInfoThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", null));
    }

    @Test
    @DisplayName("when not given first name in update info on update user, expect throws")
    public void updateUserNotGivenFirstNameUpdateInfoThrows() {
        ChatUserDto inValidChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", inValidChatUser));
    }

    @Test
    @DisplayName("when not given last name in update info on update user, expect throws")
    public void updateUserNotGivenLastNameUpdateInfoThrows() {

        ChatUserDto invalidChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", invalidChatUser));
    }

    @Test
    @DisplayName("when not given gender in update info on update user, expect throws")
    public void updateUserNotGivenGenderUpdateInfo() {
        ChatUserDto invalidChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", invalidChatUser));
    }

    @Test
    @DisplayName("when not given birth date in update info on update user, expect throws")
    public void updateUserNotGivenBirthDataUpdateInfo() {
        ChatUserDto invalidChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .lastName("Dragan")
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", invalidChatUser));
    }

    @Test
    @DisplayName("when not given valid address in update info on update user, expect throws")
    public void updateUserGivenNotValidAddressUpdateInfo() {
        ChatUserDto invalidChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> chatUserController.updateChatUser("fake1", invalidChatUser));
    }

    @Test
    @DisplayName("when valid update info data with valid address, expect save")
    public void updateUserValidWithAddress() {
        ChatUserDto validChatUser = ChatUserDto.builder()
                .chatName("chatko")
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .address(AddressDto.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .street("Dondukov")
                        .postCode("1000")
                        .build())
                .build();

        ChatUserDto updatedUserInfo =  chatUserController.updateChatUser("fake1", validChatUser).getBody();

        assertNotNull(updatedUserInfo);
        assertEquals(validChatUser.getChatName(), updatedUserInfo.getChatName());
        assertEquals(validChatUser.getFirstName(), updatedUserInfo.getFirstName());
        assertEquals(validChatUser.getLastName(), updatedUserInfo.getLastName());
        assertEquals(validChatUser.getBirthDate(), updatedUserInfo.getBirthDate());
        assertEquals(validChatUser.getGender(), updatedUserInfo.getGender());
        assertNotNull(validChatUser.getAddress());
        assertEquals(validChatUser.getAddress().getCountry(), updatedUserInfo.getAddress().getCountry());
        assertEquals(validChatUser.getAddress().getCity(), updatedUserInfo.getAddress().getCity());
        assertEquals(validChatUser.getAddress().getStreet(), updatedUserInfo.getAddress().getStreet());
        assertEquals(validChatUser.getAddress().getPostCode(), updatedUserInfo.getAddress().getPostCode());
    }

    @Test
    @DisplayName("when valid update info data, expect save")
    public void updateUserValidData() {
        ChatUserDto validChatUser = ChatUserDto.builder()
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .build();

       ChatUserDto updatedUserInfo =  chatUserController.updateChatUser("fake1", validChatUser).getBody();

        assertNotNull(updatedUserInfo);
        assertEquals(validChatUser.getChatName(), updatedUserInfo.getChatName());
        assertEquals(validChatUser.getFirstName(), updatedUserInfo.getFirstName());
        assertEquals(validChatUser.getLastName(), updatedUserInfo.getLastName());
        assertEquals(validChatUser.getBirthDate(), updatedUserInfo.getBirthDate());
        assertEquals(validChatUser.getGender(), updatedUserInfo.getGender());
        assertNull(validChatUser.getAddress());
    }

    @Test
    @DisplayName("when update user info with different username")
    public void updateUserInfoDifferentUser() {
        ChatUserDto validChatUser = ChatUserDto.builder()
                .firstName("Ivan")
                .lastName("Dragan")
                .birthDate(LocalDate.now())
                .gender(ChatUserGender.MALE)
                .build();

       assertThrows(OperationNotAllowedException.class,
               () -> chatUserController.updateChatUser("fake2" +
                       "", validChatUser));
    }

}