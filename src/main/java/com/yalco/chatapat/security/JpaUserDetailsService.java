package com.yalco.chatapat.security;

import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.repository.ChatUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final ChatUserRepository chatUserRepository;

    public JpaUserDetailsService(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ChatUser user = chatUserRepository.findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " does not exist.");
        }
        return new JpaUserDetails(user);
    }
}
