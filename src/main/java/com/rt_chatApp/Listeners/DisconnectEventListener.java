package com.rt_chatApp.Listeners;

import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        Principal principal = event.getUser();

        if (principal instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            Object principalObj = authenticationToken.getPrincipal();

            if (principalObj instanceof User user){
                userService.disconnect(user.getId());
            }
        }
    }
}
