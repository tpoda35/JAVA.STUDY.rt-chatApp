package com.rt_chatApp.Model;

import com.rt_chatApp.security.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Class and database table for the Friend requests.
 *
 * <p>Has unique constraint to prevent race condition when two user sends
 * request to each other at the same time.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "_friendRequests",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_sender_receiver",
                columnNames = {"sender_id", "receiver_id"}
        )
)
public class FriendRequest {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @ToString.Exclude
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @ToString.Exclude
    private User receiver;

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
