package com.rt_chatApp.Dto;

import com.rt_chatApp.security.user.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDto {
    private Integer userId;
    private String displayName;
    private Status status;
}
