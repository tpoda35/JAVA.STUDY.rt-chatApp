package com.rt_chatApp.Dto;

public record ChatMsgDto(
        String senderId,
        String recipientId,
        String content
) { }
