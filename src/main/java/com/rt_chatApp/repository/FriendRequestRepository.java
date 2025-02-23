package com.rt_chatApp.repository;

import com.rt_chatApp.Model.FriendRequest;
import com.rt_chatApp.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    Optional<List<FriendRequest>> findBySender(User sender);
    Optional<List<FriendRequest>> findByReceiver(User receiver);

    boolean existsBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findAllByReceiverId(Integer receiverId);

    @Query("SELECT fr FROM FriendRequest fr JOIN FETCH fr.sender JOIN FETCH fr.receiver WHERE fr.id = :id")
    Optional<FriendRequest> findByIdWithUsers(@Param("id") Integer id);
}
