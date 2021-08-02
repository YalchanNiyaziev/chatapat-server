package com.yalco.chatapat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPendingConnectionDto {
    private ChatUserDto pendingUser;
    private Instant connectionRequestTs;
}
