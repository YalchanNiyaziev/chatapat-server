package com.yalco.chatapat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatUserDto {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String chatName;
    private String firstName;
    private String lastname;
    private LocalDate birthDate;
    private ChatUserGender gender;
    private ChatUserStatus status;
    private String picture;
    private Boolean locked;
    private Boolean closed;
    private AddressDto address;

//    private List<Post> posts;
//    private List<ChatUserDto> constacts;
//    private List<ChatUserDto> blockedContacts;
}
