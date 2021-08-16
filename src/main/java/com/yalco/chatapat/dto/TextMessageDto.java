package com.yalco.chatapat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageDto {

    private String content;
    private String senderName;
    private String receiverName;
    private Instant messageTs;

}
