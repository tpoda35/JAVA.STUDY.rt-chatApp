package com.rt_chatApp.repository;

import com.rt_chatApp.Dto.FriendRequest;
import com.rt_chatApp.Enum.FriendRequestStatus;
import com.rt_chatApp.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    Optional<List<FriendRequest>> findBySender(User sender);
    Optional<List<FriendRequest>> findByReceiver(User receiver);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);
}
