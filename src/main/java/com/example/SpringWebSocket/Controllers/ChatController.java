package com.example.SpringWebSocket.Controllers;

import com.example.SpringWebSocket.Model.Body;
import com.example.SpringWebSocket.Repository.ChannelRepo;

import java.nio.channels.Channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChannelRepo channelRepo;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send")
    public void sendMessage(@Payload Body body, Authentication authentication) {
        // Prepare response message with authenticated username
        Body response = new Body(authentication.getName(), body.getContent());

        // Send to dynamic topic /topic/messages/{channelName}
        simpMessagingTemplate.convertAndSend("/topic/messages/" + body.getChannelName(), response);
    }

    @MessageMapping("/deleteChannel")
    public void deleteChannel(@Payload Body body, Authentication authentication) {
        // Broadcast channel deletion to all subscribers
        channelRepo.deleteById(body.getChannelName());
        simpMessagingTemplate.convertAndSend("/topic/delete", body);
    }
}
