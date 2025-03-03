package com.rt_chatApp.repository;

import com.rt_chatApp.model.FriendRequest;
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

    /**
     * Query for to eagerly retrieve the {@link FriendRequest} and it's receiver and sender.
     *
     * @param id for the {@link FriendRequest} to retrieve.
     * @return Optional of {@link FriendRequest}.
     */
    @Query("SELECT fr FROM FriendRequest fr JOIN FETCH fr.sender JOIN FETCH fr.receiver WHERE fr.id = :id")
    Optional<FriendRequest> findByIdWithUsers(@Param("id") Integer id);
}
