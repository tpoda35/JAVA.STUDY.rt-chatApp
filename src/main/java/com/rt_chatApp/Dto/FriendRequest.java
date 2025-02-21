package com.rt_chatApp.Dto;

import com.rt_chatApp.Enum.FriendRequestStatus;
import com.rt_chatApp.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.rt_chatApp.Enum.FriendRequestStatus.PENDING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_friendRequests")
public class FriendRequest {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        status = PENDING;
        createdAt = new Date();
    }
}
