package com.rt_chatApp.security.user;

import com.rt_chatApp.services.UsernameGenService;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Lazy;

public class UserEntityListener {

    private final UsernameGenService service;

    public UserEntityListener(@Lazy UsernameGenService service) {
        this.service = service;
    }

    @PrePersist
    public void beforeSave(User user){
        if (user.getUsername() == null){
            user.setUsername(service.generateUsername());
        }
    }

}
