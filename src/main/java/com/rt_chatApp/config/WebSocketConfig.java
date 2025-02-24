package com.rt_chatApp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

/**
 * Configuration for Websocket connections.
 *
 * <p>It enables the STOMP Websocket messages through the whole application.
 * Configures the Websocket endpoint, message brokers for broadcasting and private messaging.
 * Also configures a JSON serializer.</p>
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomHandshakeHandler handshakeHandler;

    /**
     * Enables the connections endpoint where clients can connect with SockJS.
     * Also enables the {@link CustomHandshakeHandler}.
     *
     * @param registry for to register websockets.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(handshakeHandler)
                .withSockJS();
    }

    /**
     * Configures the routing of the messages.
     *
     * <p>Configuration details:
     * <ul>
     *   <li><b>/topic</b>: For broadcasting messages to all connected clients.</li>
     *   <li><b>/user</b>: For sending private messages to users.</li>
     *   <li><b>/app</b>: Prefix for client-to-server messages mapped to {@code @MessageMapping} methods.</li>
     *   <li><b>/user</b>: For private communication.</li>
     * </ul>
     * </p>
     *
     * @param registry for configuring routes.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Configure a custom JSON serializer.
     *
     * @param messageConverters a list of messageConverters to register.
     * @return false, which means that default converters shouldn't be overridden.
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }
}
