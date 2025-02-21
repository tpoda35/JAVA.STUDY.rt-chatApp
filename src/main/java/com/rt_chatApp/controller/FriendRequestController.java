package com.rt_chatApp.controller;

import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;

    @PostMapping("/addFriend")
    public CompletableFuture<ResponseEntity<Void>> addFriend(
            @RequestParam String uniqueName
    ) {
        friendRequestService.sendFriendRequest(uniqueName);

        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

    @PostMapping("/acceptRequest")
    public CompletableFuture<ResponseEntity<Void>> acceptRequest(
            @RequestParam Integer requestId
    ) {

        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

}
