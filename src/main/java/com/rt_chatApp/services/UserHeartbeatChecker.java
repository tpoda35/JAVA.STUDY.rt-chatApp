package com.rt_chatApp.services;

import com.rt_chatApp.Handler.WebSocketSessionTracker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserHeartbeatChecker {

    private final WebSocketSessionTracker webSocketHandler;
    private final static Logger logger = LoggerFactory.getLogger(UserHeartbeatChecker.class);

    @Scheduled(fixedRate = 10000)
    public void checkOnlineUsers(){
        Map<String, WebSocketSession> sessions = webSocketHandler.getActiveSessions();
        Iterator<Map.Entry<String, WebSocketSession>> iterator = sessions.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            WebSocketSession session = entry.getValue();

            if (session.isOpen()){
                try {
                    session.sendMessage(new PingMessage(ByteBuffer.wrap("ping".getBytes())));
                    logger.info("Heartbeat: Sent heartbeat to: {}", entry.getKey());
                } catch (IOException e) {
                    logger.info("Heartbeat: User marked offline(id: {}).", entry.getKey());
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
    }
}
