package com.rt_chatApp.services;

import com.rt_chatApp.Dto.FriendRequest;
import com.rt_chatApp.repository.FriendRequestRepository;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import com.rt_chatApp.security.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.rt_chatApp.Enum.FriendRequestStatus.PENDING;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;

    public CompletableFuture<Void> sendFriendRequest(String uniqueName) {
        int senderId = userService.getUser().getId();

        return CompletableFuture.supplyAsync(() -> transactionTemplate.execute(status -> {
            User receiver = userRepository.findByUniqueIdentifier(uniqueName)
                    .orElseThrow();

            User sender = userRepository.findById(senderId)
                    .orElseThrow();

            List<User> friends = sender.getFriends();
            if (friends.contains(receiver) || hasPendingRequest(sender, receiver)) {
                return null;
            }
            requestRepository.save(
                FriendRequest.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .build()
            );

            return null;
        }));
    }

    private boolean hasPendingRequest(User sender, User receiver){
        return requestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, PENDING) ||
                requestRepository.existsBySenderAndReceiverAndStatus(receiver, sender, PENDING);
    }
}
