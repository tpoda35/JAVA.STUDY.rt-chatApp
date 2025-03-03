package com.rt_chatApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for a chat message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {

    private Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String content;

}
