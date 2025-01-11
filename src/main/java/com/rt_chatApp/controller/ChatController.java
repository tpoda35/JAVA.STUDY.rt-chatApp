package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.ChatMsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void handleMessage(ChatMsgDto chatMessage) {
        String destination = "/queue/chat/" + chatMessage.recipientId();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

}
