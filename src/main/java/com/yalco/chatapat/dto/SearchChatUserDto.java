package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import com.yalco.chatapat.enums.UserRole;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchChatUserDto {
    private String username;
    private String chatName;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private ChatUserGender gender;
    private UserRole role;
    private ChatUserStatus status;
    private Boolean locked;
    private Boolean closed;
    private Instant registerBeforeTs;
    private Instant registerAfterTs;
    private AddressDto address;
}
