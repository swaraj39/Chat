package com.example.SpringWebSocket.Controllers;

import com.example.SpringWebSocket.Model.Body;

import com.example.SpringWebSocket.Model.PrivateMessage;
import com.example.SpringWebSocket.Model.Typing;
import com.example.SpringWebSocket.Model.Whom;
import com.example.SpringWebSocket.Repository.ChannelRepo;

import java.nio.channels.Channel;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ChatController {

    @Autowired
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

    @MessageMapping("/chat.private")
    public void sendPrivate(@Payload PrivateMessage privateMessage, Principal principal, Authentication authentication) {
        
        if (principal==null) {
            System.out.println("no principal");
            return;
        }
        String sender = principal.getName();      // sender username
        String recipient = privateMessage.getTo();     // recipient username

        System.out.println("Sender: " + sender + ", Recipient: " + recipient + ", Msg: " + privateMessage.getContent());

        System.out.println(principal.getName());
        Whom response = new Whom(sender, privateMessage.getContent());
        try {
    simpMessagingTemplate.convertAndSendToUser(recipient, "/queue/messages", response);
} catch (Exception e) {
    System.err.println("Failed to send private message: " + e.getMessage());
}
}

    @MessageMapping("/typing")
    public void typing(@Payload Typing typing, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("no auth");
            return;
        }
        String username = authentication.getName();
        String channelName = typing.getChannelName();

        Map<String, String> payload = new HashMap<>();
        payload.put("from", username);
        payload.put("channelName", channelName);

        simpMessagingTemplate.convertAndSend("/topic/typing/" + channelName, payload);
    }

    public void joinMessage(String channelName, String username) {
        Map<String, String> payload = new HashMap<>();
        payload.put("user", username);
        simpMessagingTemplate.convertAndSend("/topic/join" + channelName, payload);
    }
}
