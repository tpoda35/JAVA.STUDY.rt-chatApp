package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.FriendRequestDto;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(FriendRequestController.class);

    @PostMapping("/addFriend")
    public CompletableFuture<Void> addFriend(
            @RequestParam String receiverUniqueName
    ) {
        return friendRequestService.sendFriendRequest(receiverUniqueName,  userService.getUser().getId());
    }

    @GetMapping("/acceptRequest")
    public CompletableFuture<ResponseEntity<Void>> acceptRequest(
            @RequestParam Integer requestId
    ) {

        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

    @PostMapping("/declineRequest")
    public CompletableFuture<ResponseEntity<Void>> declineRequest(
            @RequestParam Integer requestId
    ){
        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

    @GetMapping("/getReceivedRequests")
    public CompletableFuture<List<FriendRequestDto>> getReceivedRequests(
            Authentication authentication
    ){
        User user = (User) authentication.getPrincipal();
        return null;
//        return friendRequestService.getReceivedRequests(user.getId());
    }

}
