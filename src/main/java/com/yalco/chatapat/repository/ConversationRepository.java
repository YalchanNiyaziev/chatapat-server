package com.yalco.chatapat.repository;

import com.yalco.chatapat.entity.ChatUser;
import com.yalco.chatapat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

//    Optional<Conversation> findFirstByParticipantsIn(List<Set<ChatUser>> participants);

    @Query("select c from Conversation c join c.participants p where p.username = :username")
    Optional<Conversation> findFirstByParticipantsIn(@Param("username") String participants);

    List<Conversation> findAllByParticipantsUsername(String username);

    @Query("select c from Conversation c  join c.participants p where p.username in :participants group by c")
    List<Conversation> findFirstByParticipantsIn(@Param("participants") List<String> usernames);

    @Query("select c from Conversation c join c.participants p where p.username in :usernames " +
            "group by c having count(distinct p.id) = :participantsCount")
    List<Conversation> getConversationByParticipantsUsername(@Param("usernames") List<String> usernames, @Param("participantsCount") Long participantsCount);
}
