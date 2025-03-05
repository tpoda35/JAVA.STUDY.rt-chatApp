package com.rt_chatApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rt_chatApp.model.base.Auditable;
import com.rt_chatApp.security.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "_notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends Auditable {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @JsonIgnore
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
}
