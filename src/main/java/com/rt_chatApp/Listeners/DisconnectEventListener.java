package com.rt_chatApp.Listeners;

import com.rt_chatApp.security.user.User;
import com.rt_chatApp.services.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * Class to track when the user disconnects from the websocket server.
 */
@Component
@RequiredArgsConstructor
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final UserStatusService statusService;

    /**
     * Triggers when a user disconnects from the websocket server.
     *
     * <p>Gets the user from the event, sets it's status to offline
     * and then sends message about it for the user friends.</p>
     */
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        Principal principal = event.getUser();

        if (principal instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            Object principalObj = authenticationToken.getPrincipal();

            if (principalObj instanceof User user) {
                statusService.userOffline(user.getId())
                        .thenCompose(aVoid -> statusService.notifyFriends(user.getId()));
            }
        }
    }
}
