package com.example.SpringWebSocket.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .setHandshakeHandler(new DefaultHandshakeHandler())
            .setAllowedOrigins("*"); // keep both for compatibility
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
            .setHeartbeatTime(10000); // <== helps SockJS reconnect on timeout
}


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // messages prefixed with /app go to controllers
         // Create a scheduler for heartbeats
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();
        registry.setApplicationDestinationPrefixes("/app");
        // messages prefixed with /topic go to message broker
        registry.enableSimpleBroker("/topic","/queue")
                .setTaskScheduler(taskScheduler)
                .setHeartbeatValue(new long[]{10000, 10000}); // send heartbeat every 10 seconds
        // for the messages sent to specific user
        registry.setUserDestinationPrefix("/user");
    }
}
