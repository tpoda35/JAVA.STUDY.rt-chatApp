package com.rt_chatApp.ChatRoom;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_chatRoom")
public class ChatRoom {

    @Id
    private String id;
    private String chatId;
    private Integer senderId;
    private Integer recipientId;
}
