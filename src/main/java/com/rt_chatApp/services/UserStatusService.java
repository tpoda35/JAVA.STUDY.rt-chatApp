package com.rt_chatApp.services;

import com.rt_chatApp.dto.FriendDto;
import com.rt_chatApp.exception.UserNotFoundException;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import com.rt_chatApp.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.rt_chatApp.Enum.FriendDtoType.FRIEND_UPDATE;
import static com.rt_chatApp.security.user.Status.OFFLINE;
import static com.rt_chatApp.security.user.Status.ONLINE;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public CompletableFuture<Void> userOnline(Integer id) {
        transactionTemplate.execute(status -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found when setting it's status to online."));
            user.setStatus(ONLINE);
            userRepository.save(user);

           return null;
        });

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> userOffline(Integer id) {
        transactionTemplate.execute(status -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found when setting it's status to offline."));
            user.setStatus(OFFLINE);
            userRepository.save(user);

            return null;
        });

        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> notifyFriends(Integer id){
        transactionTemplate.execute(status -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found when sending status change message."));
            List<User> friends = user.getFriends();
            for (User friend : friends) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(friend.getId()),
                        "/queue/friends",
                        FriendDto.builder()
                                .type(FRIEND_UPDATE)
                                .userId(user.getId())
                                .displayName(user.getDisplayName())
                                .status(user.getStatus())
                                .build());
            }

            return null;
        });

        return CompletableFuture.completedFuture(null);
    }

}
