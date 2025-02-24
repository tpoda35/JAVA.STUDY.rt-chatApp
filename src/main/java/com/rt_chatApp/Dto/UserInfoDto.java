package com.rt_chatApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object to transfer specific user data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private Integer userId;
    private String displayName;
}
