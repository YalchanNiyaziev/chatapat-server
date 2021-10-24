package com.yalco.chatapat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCredentialsRequest {
    private String oldPass;
    private String newPass;
    private String newPassConfirm;
}
