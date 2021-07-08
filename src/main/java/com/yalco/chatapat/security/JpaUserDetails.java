package com.yalco.chatapat.security;

import com.yalco.chatapat.entity.ChatUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class JpaUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Boolean enabled;
    private final Boolean locked;


    public JpaUserDetails(ChatUser chatUser) {
        this.username = chatUser.getUsername();
        this.password = chatUser.getPassword();
        this.enabled = !chatUser.getClosed();
        this.locked = chatUser.getLocked();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("AAA"), new SimpleGrantedAuthority("BBB")));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
