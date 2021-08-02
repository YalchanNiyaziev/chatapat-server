package com.yalco.chatapat.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_user_connections")
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private ChatUser requester;

    @ManyToOne
    @JoinColumn(name = "bearer_id")
    private ChatUser bearer;

    @Column(name = "is_connection_request", nullable = false)
    private boolean connectionRequest;

    @Column(nullable = false)
    private boolean connected;

    @Column(nullable = false)
    private boolean blocked;

    @Column(name = "last_update_ts", nullable = false)
    private Instant lastUpdateTs;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
}
