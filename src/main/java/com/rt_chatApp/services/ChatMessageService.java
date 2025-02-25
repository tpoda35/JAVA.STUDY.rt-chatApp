package com.rt_chatApp.services;

import com.rt_chatApp.Model.ChatMessage;
import com.rt_chatApp.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling chat messages. (Will be modified and optimized)
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService service;

    /**
     * Method, which saves the incoming chat message.
     *
     * @param chatMessage incoming message.
     * @return the incoming {@link ChatMessage}.
     */
    public ChatMessage save(ChatMessage chatMessage){
        var chatId = service.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // Will be implemented.

        chatMessage.setChatId(chatId);
        return repository.save(chatMessage);
    }

    /**
     * Method, which finds the chat message history between two user. (Will be optimized)
     *
     * @param senderId the id of the user who sent in the message.
     * @param recipientId the id of the user who got the message.
     * @return list of {@link ChatMessage}.
     */
    public List<ChatMessage> findChatMessages(
            Integer senderId, Integer recipientId
    ) {
        var chatId = service.getChatRoomId(senderId, recipientId, false);

        return chatId.map(repository::findByChatId)
                .orElse(new ArrayList<>());
    }
}
