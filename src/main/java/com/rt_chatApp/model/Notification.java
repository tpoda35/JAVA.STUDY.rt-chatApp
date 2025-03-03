package com.rt_chatApp.model;

import com.rt_chatApp.model.base.Auditable;
import com.rt_chatApp.security.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "_notifications")
public class Notification extends Auditable {

    @Id
    @GeneratedValue
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recipient;

}
