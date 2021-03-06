package com.yalco.chatapat.repository;

import com.yalco.chatapat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long>, JpaSpecificationExecutor<ChatUser> {

    Optional<ChatUser> findChatUserByUsername(String username);

    Set<ChatUser> findAllByUsernameIn(List<String> usernames);

//    public List<ChatUser> getChatUsers() {
//        List<ChatUser> users = new ArrayList<>();
//        ChatUser one = new ChatUser();
//        one.setUsername("Admin");
//        one.setPassword("1234");
//        one.setRole(UserRole.ADMIN);
//        one.setLocked(false);
//        one.setClosed(false);
//
//        ChatUser two = new ChatUser();
//        two.setRole(UserRole.STANDARD_USER);
//        two.setUsername("Standart_user");
//        two.setPassword("123");
//        two.setClosed(false);
//        two.setLocked(false);
//
//        users.add(one);
//        users.add(two);
//        return users;
//    }
}
