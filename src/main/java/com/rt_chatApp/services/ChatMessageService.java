package com.rt_chatApp.services;

import com.rt_chatApp.Model.ChatMessage;
import com.rt_chatApp.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService service;

    public ChatMessage save(ChatMessage chatMessage){
        var chatId = service.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // Will be implemented.

        chatMessage.setChatId(chatId);
        return repository.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(
            Integer senderId, Integer recipientId
    ) {
        var chatId = service.getChatRoomId(senderId, recipientId, false);

        return chatId.map(repository::findByChatId)
                .orElse(new ArrayList<>());
    }


}
