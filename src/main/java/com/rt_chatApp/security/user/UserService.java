package com.rt_chatApp.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.rt_chatApp.security.user.Status.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public void disconnect(Integer id){
        var storedUser = repository.findById(id)
                .orElse(null);
        if (storedUser != null){
            storedUser.setStatus(OFFLINE);
            repository.save(storedUser);
        }
    }

    public void connect(Integer id) {
        var storedUser = repository.findById(id)
                .orElse(null);
        if (storedUser != null){
            storedUser.setStatus(ONLINE);
            repository.save(storedUser);
        }
    }

    public void idle(Integer id) {
        var storedUser = repository.findById(id)
                .orElse(null);
        if (storedUser != null){
            storedUser.setStatus(IDLE);
            repository.save(storedUser);
        }
    }
}
