package com.yalco.chatapat.entity;


import com.yalco.chatapat.enums.MessageStatus;
import com.yalco.chatapat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_conversation_messages")
public class ConversationMessage extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType type;

    @Column(nullable = false)
    private String content;


    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", nullable = false)
    private MessageStatus status;

    @Column(nullable = false, name = "message_ts")
    private Instant messageTs;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ChatUser sender;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

//    @OneToOne
//    private ChatMessage replyTo;
}
