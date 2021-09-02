package com.yalco.chatapat.dto;

import com.yalco.chatapat.enums.ChatUserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;
    private ChatUserGender gender;
    private LocalDate birthDate;
}
