package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.FriendDto;
import com.rt_chatApp.Dto.FriendRequestDto;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @PostMapping("/addFriend")
    public CompletableFuture<Void> addFriend(
            @RequestParam String receiverUniqueName
    ) {
        return friendService.sendFriendRequest(receiverUniqueName,  userService.getUser().getId());
    }

    @PostMapping("/acceptRequest/{requestId}")
    public CompletableFuture<ResponseEntity<Void>> acceptRequest(
            @PathVariable("requestId") Integer requestId
    ) {
        friendService.acceptRequest(requestId);
        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

    @PostMapping("/rejectRequest/{requestId}")
    public CompletableFuture<ResponseEntity<Void>> rejectRequest(
            @PathVariable("requestId") Integer requestId
    ){
        friendService.rejectRequest(requestId);
        return CompletableFuture.completedFuture(
                ResponseEntity.ok().build()
        );
    }

    @GetMapping("/getReceivedRequests")
    public CompletableFuture<List<FriendRequestDto>> getReceivedRequests(){
        return friendService.getReceivedRequests(userService.getUser().getId());
    }

    @GetMapping("/getAllFriend")
    public CompletableFuture<List<FriendDto>> getAllFriend(){
        return friendService.getAllFriend();
    }

}
