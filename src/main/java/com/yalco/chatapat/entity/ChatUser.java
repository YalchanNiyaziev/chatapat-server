package com.yalco.chatapat.entity;

import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser extends BaseUser{
    private ChatUserGender gender;
    private ChatUserStatus status;
    private Boolean locked;
    private Boolean closed;

//    private List<Post> posts;
//    private List<ChatUserDto> constacts;
//    private List<ChatUserDto> blockedContacts;
}
