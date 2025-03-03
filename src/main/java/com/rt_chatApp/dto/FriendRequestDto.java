package com.rt_chatApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data transfer object for the friend requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestDto {
    private Integer id;
    private String senderName;
    private String receiverName;
    private Date createdAt;
}
