package com.example.SpringWebSocket.Configuration;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * This ensures each WebSocket connection has a fixed username (Principal)
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // Extract the username from query param (?username=...)
        String query = request.getURI().getQuery();
        final String username;
        if (query != null && query.startsWith("username=")) {
            username = query.substring("username=".length());
        } else {
            username = "anonymous";
        }
        return () -> username;
    }
}
