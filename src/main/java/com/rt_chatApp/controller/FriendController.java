package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.FriendDto;
import com.rt_chatApp.Dto.FriendRequestDto;
import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller class for the friend system.
 */

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(FriendController.class);

    /**
     * Endpoint for friend adding.
     *
     * <p>Sends a friend request for the given user in an asynchronous way.</p>
     *
     * @param receiverUniqueName is the custom name of the receiver of the request(name#123).
     */
    @PostMapping("/addFriend")
    public CompletableFuture<Void> addFriend(
            @RequestParam String receiverUniqueName
    ) {
        return friendService.sendFriendRequest(receiverUniqueName,  userService.getUser().getId());
    }

    /**
     * Endpoint for accepting a request.
     *
     * @param requestId is the id of the given request.
     */
    @PostMapping("/acceptRequest/{requestId}")
    public CompletableFuture<Void> acceptRequest(
            @PathVariable("requestId") Integer requestId
    ) {
        return friendService.acceptRequest(requestId);
    }

    /**
     * Endpoint for rejecting a request.
     *
     * @param requestId is the id of the given request.
     */
    @PostMapping("/rejectRequest/{requestId}")
    public CompletableFuture<Void> rejectRequest(
            @PathVariable("requestId") Integer requestId
    ){
        return friendService.rejectRequest(requestId);
    }

    /**
     * Endpoint to get the received requests.
     *
     * @return a list of {@link FriendRequestDto}.
     */
    @GetMapping("/getReceivedRequests")
    public CompletableFuture<List<FriendRequestDto>> getReceivedRequests(){
        return friendService.getReceivedRequests(userService.getUser().getId());
    }

    /**
     * Endpoint to get all the friend of the current authenticated user.
     *
     * @return a list of {@link FriendDto}.
     */
    @GetMapping("/getAllFriend")
    public CompletableFuture<List<FriendDto>> getAllFriend(){
        return friendService.getAllFriend();
    }

}
