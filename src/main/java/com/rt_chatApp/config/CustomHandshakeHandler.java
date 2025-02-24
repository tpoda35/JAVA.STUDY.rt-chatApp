package com.rt_chatApp.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Custom Websocket handshake handler, that connects
 * the authenticated user with the Websocket.
 *
 * <p>It ensures that the Websocket connection is linked with the
 * authenticated user, enabling better security for the connections.</p>
 */

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * Determines the authenticated user at handshake.
     *
     * @param request the current http request.
     * @param wsHandler websocket handler that will handle the request.
     * @param attributes handshake attributes.
     * @return user authentication details.
     */
    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
