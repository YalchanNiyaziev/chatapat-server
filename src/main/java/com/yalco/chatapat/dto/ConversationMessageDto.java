package com.yalco.chatapat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yalco.chatapat.enums.MessageStatus;
import com.yalco.chatapat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationMessageDto {

//    private Long messageId;

    private String content;

    private String receiverName;

    private Instant messageTs;

    //TODO remove or resolve if websoclet message fails
    private MessageType type;

    private ChatUserDto senderInfo;

    // TODO remove after allow ws comunication
    private String senderName;

    private Long conversationId;

    private MessageStatus messageStatus;

}
