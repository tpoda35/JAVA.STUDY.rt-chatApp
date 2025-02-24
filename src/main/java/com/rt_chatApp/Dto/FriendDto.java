package com.rt_chatApp.Dto;

import com.rt_chatApp.Enum.FriendDtoType;
import com.rt_chatApp.security.user.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDto {

    public FriendDto(Integer userId, String displayName, Status status) {
        this.userId = userId;
        this.displayName = displayName;
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    private FriendDtoType type;
    private Integer userId;
    private String displayName;
    private Status status;
}
