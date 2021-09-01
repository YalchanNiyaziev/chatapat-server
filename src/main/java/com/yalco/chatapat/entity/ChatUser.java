package com.yalco.chatapat.entity;

import com.yalco.chatapat.enums.ChatUserGender;
import com.yalco.chatapat.enums.ChatUserStatus;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_users")
public class ChatUser extends BaseUser{

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatUserGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatUserStatus status;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private Boolean locked;

    @Column(nullable = false)
    private Boolean closed;

    @Column(name = "registration_ts", nullable = false)
    private Instant registrationTs;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "chat_user_x_conversation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_id")
    )
    private Set<Conversation> conversations;

//    private List<Post> posts;
//    private List<ChatUserDto> constacts;
//    private List<ChatUserDto> blockedContacts;
}
