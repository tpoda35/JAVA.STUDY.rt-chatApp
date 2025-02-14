package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.ChatMessage;
import com.rt_chatApp.Dto.ChatNotification;
import com.rt_chatApp.security.user.Status;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.ChatMessageService;
import com.rt_chatApp.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatService chatService;

    private final UserService userService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSenderId())
                        .recipientId(savedMsg.getRecipientId())
                        .content(savedMsg.getContent())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
             @PathVariable("senderId") Integer senderId,
             @PathVariable("recipientId") Integer recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(chatService.findConnectedUsers());
    }

    @GetMapping("/isOnline/{id}")
    public Status findUser(
            @PathVariable("id") Integer id
    ){
        return userService.getUser().getStatus();
    }
}
