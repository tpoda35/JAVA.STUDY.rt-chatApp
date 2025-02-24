package com.rt_chatApp.services;

import com.rt_chatApp.Dto.FriendDto;
import com.rt_chatApp.Dto.FriendRequestDto;
import com.rt_chatApp.Exceptions.UserNotFoundException;
import com.rt_chatApp.Mapper.FriendRequestMapper;
import com.rt_chatApp.Model.FriendRequest;
import com.rt_chatApp.repository.FriendRequestRepository;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import com.rt_chatApp.security.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.rt_chatApp.Enum.FriendDtoType.FRIEND_ADD;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Async
    public CompletableFuture<Void> sendFriendRequest(String uniqueName, int senderId) {
        transactionTemplate.execute(status -> {
            User receiver = userRepository.findByUniqueIdentifier(uniqueName)
                    .orElseThrow(() -> new UserNotFoundException("Receiver not found."));

            if (receiver.getId() == senderId) {
                throw new IllegalStateException("You can't send friend request for yourself.");
            }

            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("Sender not found."));

            List<User> friends = sender.getFriends();
            if (friends.contains(receiver) || hasPendingRequest(sender, receiver)) {
                throw new IllegalStateException("Duplicate request or already friends.");
            }

            requestRepository.save(
                    FriendRequest.builder()
                            .sender(sender)
                            .receiver(receiver)
                            .build()
            );
            return null;
        });

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<FriendRequestDto>> getReceivedRequests(
            Integer userId
    ) {
        List<FriendRequest> friendRequests = requestRepository.findAllByReceiverId(userId);
        if (friendRequests.isEmpty()){
            throw new EntityNotFoundException("There's no received requests.");
        }
        return CompletableFuture.completedFuture(FriendRequestMapper.INSTANCE.toDtoList(friendRequests));
    }

    @Async
    public CompletableFuture<Void> acceptRequest(
            Integer id
    ) {
        transactionTemplate.execute(status -> {
            FriendRequest request = requestRepository.findByIdWithUsers(id)
                    .orElseThrow(() -> new EntityNotFoundException("Friend request not found."));

            User sender = request.getSender();
            User receiver = request.getReceiver();

            sender.addFriend(receiver);
            receiver.addFriend(sender);

            userRepository.saveAll(Arrays.asList(sender, receiver));

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(sender.getId()),
                    "/queue/friends",
                    FriendDto.builder()
                            .type(FRIEND_ADD)
                            .userId(receiver.getId())
                            .displayName(receiver.getDisplayName())
                            .status(receiver.getStatus())
                            .build()
            );

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(receiver.getId()),
                    "/queue/friends",
                    FriendDto.builder()
                            .type(FRIEND_ADD)
                            .userId(sender.getId())
                            .displayName(sender.getDisplayName())
                            .status(sender.getStatus())
                            .build()
            );

            requestRepository.delete(request);

            return null;
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> rejectRequest(
            Integer id
    ) {
        transactionTemplate.execute(status -> {
            FriendRequest request = requestRepository.findByIdWithUsers(id)
                    .orElseThrow(() -> new EntityNotFoundException("Friend request not found."));

            requestRepository.delete(request);

           return null;
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<FriendDto>> getAllFriend(){
        List<FriendDto> friends = transactionTemplate.execute(status -> {
            User user = userService.getUser();
            if (user == null){
                throw new UserNotFoundException("User not found when getting friends.");
            }
            List<FriendDto> friendsList = userRepository.findFriendsByUserId(user.getId());

            if (friendsList == null || friendsList.isEmpty()){
                throw new UserNotFoundException("User has no friends.");
            }

            return friendsList;
        });

        return CompletableFuture.completedFuture(friends);
    }

    private boolean hasPendingRequest(User sender, User receiver){
        return requestRepository.existsBySenderAndReceiver(sender, receiver) ||
                requestRepository.existsBySenderAndReceiver(receiver, sender);
    }
}
