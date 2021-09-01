package com.yalco.chatapat.repository;

import com.yalco.chatapat.entity.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {
    Optional<ConversationMessage> findTopByConversationIdOrderByMessageTsDesc(Long conversationId);
    List<ConversationMessage> findAllByConversationIdOrderByMessageTsAsc(Long conversationId);
}
