package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserStatus;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionDto {

    private Long id;
    private ChatUserDto partner;
    private Boolean blocked;
    private Boolean connected;
    private Boolean connectionRequested;
    private String updatedBy;
    private Instant connectionRequestTs;

}
