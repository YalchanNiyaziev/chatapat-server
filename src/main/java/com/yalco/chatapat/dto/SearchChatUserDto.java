package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserGender;
import lombok.*;

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
    private String lastname;
    private LocalDate birthDate;
    private ChatUserGender gender;
    private AddressDto address;
}
