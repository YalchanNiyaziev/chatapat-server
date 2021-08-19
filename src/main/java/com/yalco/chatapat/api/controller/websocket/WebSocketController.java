package com.yalco.chatapat.api.controller.websocket;


import com.yalco.chatapat.dto.TextMessageDto;
import com.yalco.chatapat.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ConversationService conversationService;

    @MessageMapping("/message")
    public void processMessage(@Payload TextMessageDto message) {
        conversationService.saveUserSpecificTextMessage(message);
        messagingTemplate.convertAndSendToUser(message.getReceiverName(), "/queue/messages", message);
    }
}
