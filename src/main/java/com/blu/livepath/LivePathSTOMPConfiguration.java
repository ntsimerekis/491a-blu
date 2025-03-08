package com.blu.livepath;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class LivePathSTOMPConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //We only really use "topic" just keeping the others
        config.enableSimpleBroker("/topic/", "/queue/", "/message/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Register a STOMP websocket endpoint /ws/livepath
        registry.addEndpoint("/ws/livepath").setAllowedOriginPatterns("http://localhost:5173");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(4 * 8192);
        registry.setTimeToFirstMessage(30000);
    }

}