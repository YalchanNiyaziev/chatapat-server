package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDto {
    private Long id;
    private String username;
    private String chatName;
    private ChatUserGender gender;
    private ChatUserStatus status;
    private Boolean locked;
    private Boolean closed;

//    private List<Post> posts;
//    private List<ChatUserDto> constacts;
//    private List<ChatUserDto> blockedContacts;
}
