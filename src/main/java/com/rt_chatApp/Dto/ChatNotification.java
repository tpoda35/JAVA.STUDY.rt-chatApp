package com.rt_chatApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
