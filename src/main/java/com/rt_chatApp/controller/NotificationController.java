package com.rt_chatApp.controller;

import com.rt_chatApp.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @GetMapping("/getAll/{id}")
    public CompletableFuture<List<NotificationDto>> getAllNotificationByUser(
            @PathVariable Integer userId
    ) {
        return null;
    }

}
