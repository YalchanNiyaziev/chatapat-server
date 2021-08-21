package com.yalco.chatapat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ConversationMessageDto {

    private String content;
    private String senderName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiverName;

    private Instant messageTs;

    //TODO remove or resolve if websoclet message fails
    private MessageType type;

}
