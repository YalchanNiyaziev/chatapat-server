package com.yalco.chatapat.entity;

import com.yalco.chatapat.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_conversations")
public class Conversation extends BaseEntity{

    @Column(nullable = false, name = "started_ts")
    private Instant startedTs;

    @Column(nullable = false, name = "end_ts")
    private Instant endTs;

    @Enumerated(EnumType.STRING)
    @Column(name = "conversation_type", nullable = false)
    private ConversationType type;

    @Column(nullable = false)
    private Boolean vanishable;

    @ManyToMany(mappedBy = "conversations")
    private Set<ChatUser> participants;

    public void addParticipant(ChatUser user) {
        this.participants.add(user);
        user.getConversations().add(this);
    }

    public void removeParticipants(ChatUser user) {
        this.participants.remove(user);
        user.getConversations().remove(this);
    }
}
