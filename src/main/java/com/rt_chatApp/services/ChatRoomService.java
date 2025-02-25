package com.rt_chatApp.services;

import com.rt_chatApp.ChatRoom.ChatRoom;
import com.rt_chatApp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the private chatroom system.
 */
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * Method, which either gives back a chatroomId if it's exists,
     * or creates a new, if the boolean is set to true, otherwise it
     * will return an Optional.empty()
     *
     * @param senderId the id of the sender.
     * @param recipientId the id of the recipient.
     * @param createNewRoomIfNotExists determines if a new chatroom shall be created.
     * @return Optional of String.
     */
    public Optional<String> getChatRoomId(
            Integer senderId,
            Integer recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(()-> {
                   if (createNewRoomIfNotExists){
                        var chatId = createChat(senderId, recipientId);

                        return Optional.of(chatId);
                   }

                   return Optional.empty();
                });
    }

    private String createChat(Integer senderId, Integer recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();


        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

}
