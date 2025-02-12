package com.rt_chatApp.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionTracker extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionTracker.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.put(session.getId(), session);
        logger.info("Heartbeat: User connected with the id of {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if ("heartbeat".equals(message.getPayload())){
            logger.info("Heartbeat: Received heartbeat from the id of: {}", session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.remove(session.getId(), session);
        logger.info("Heartbeat: User disconnected with the id of {}", session.getId());
    }

    public Map<String, WebSocketSession> getActiveSessions(){
        return activeSessions;
    }
}
