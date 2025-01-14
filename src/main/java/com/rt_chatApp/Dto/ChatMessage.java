package com.rt_chatApp.Dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_chatMessage")
public class ChatMessage {

    @Id
    private String id;
    private String chatId;
    private Integer senderId;
    private Integer recipientId;
    private String content;
    private Date timeStamp;

}
