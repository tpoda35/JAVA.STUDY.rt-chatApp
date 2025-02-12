package com.rt_chatApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/connection")
public class HeartbeatController {

    private final static Logger logger = LoggerFactory.getLogger(HeartbeatController.class);

    @MessageMapping("/heartbeat")
    @SendTo("/topic/online-users")
    public String receiveHeartbeat(
            @Payload String message
    ) {
        logger.info("Heartbeat: Received heartbeat: {}", message);
        return "User is online.";
    }

}
