package com.rt_chatApp.Listeners;

import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserService;
import com.rt_chatApp.services.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    private final UserService userService;
    private final UserStatusService statusService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        Principal principal = event.getUser();

        if (principal instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            Object principalObj = authenticationToken.getPrincipal();

            if (principalObj instanceof User user) {
                statusService.addUser(user.getId());
                userService.connect(user.getId());

                CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(() -> {
                    messagingTemplate.convertAndSend("/topic/onlineUsers", statusService.getOnlineUsers());
                });
            }
        }
    }
}
