package com.rt_chatApp.listener;

import com.rt_chatApp.security.user.User;
import com.rt_chatApp.services.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

/**
 * Class to track when the user connects to the websocket server.
 */
@Component
@RequiredArgsConstructor
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    private final UserStatusService statusService;

    /**
     * Triggers when a user connects to the websocket server.
     *
     * <p>Gets the user from the event, sets it's status to online
     * and then sends message about it for the user friends.</p>
     */
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        Principal principal = event.getUser();

        if (principal instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            Object principalObj = authenticationToken.getPrincipal();

            if (principalObj instanceof User user) {
                statusService.userOnline(user.getId())
                        .thenCompose(aVoid -> statusService.notifyFriends(user.getId()));
            }
        }
    }
}
