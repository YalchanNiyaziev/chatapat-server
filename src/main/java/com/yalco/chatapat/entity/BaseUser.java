package com.yalco.chatapat.entity;


import com.yalco.chatapat.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser extends BaseEntity{

    private String username;
    private String password;
    private UserRole role;
}
//TODO make it abstract
