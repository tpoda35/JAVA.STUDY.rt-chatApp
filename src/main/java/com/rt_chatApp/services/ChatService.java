package com.rt_chatApp.services;

import com.rt_chatApp.security.user.Status;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository repository;

    public List<User> findConnectedUsers(){
        return repository.findAllByStatus(Status.ONLINE);
    }
}
