package com.rt_chatApp.services;

import com.rt_chatApp.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    private static final ConcurrentHashMap<Integer, String> onlineUsers = new ConcurrentHashMap<>();

    public void addUser(Integer id){
        userRepository.findById(id).ifPresent(user -> onlineUsers.put(user.getId(), user.getFirstname()));
        var users = onlineUsers;
    }

    public void removeUser(Integer id) {
        userRepository.findById(id).ifPresent(user -> onlineUsers.remove(user.getId()));
        var users = onlineUsers;
    }

    public ConcurrentHashMap<Integer, String> getOnlineUsers() {
        return onlineUsers;
    }

}
