package com.example.SpringWebSocket.Controllers;

import com.example.SpringWebSocket.Model.Channel;
import com.example.SpringWebSocket.Model.Users;
import com.example.SpringWebSocket.Repository.ChannelRepo;
import com.example.SpringWebSocket.Repository.UserRepo;
import com.example.SpringWebSocket.Services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class FrontController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ChannelRepo channelRepo;
    @Autowired
    private ChatController chatController;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String home() {
        return "LoginAndSignup";
    }

    @RequestMapping("/asa")
    public String asa() {
        return "LoginAndSignup";
    }

    @RequestMapping("/home")
    public String hello(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            //System.out.println("no");
            return "LonginAndSignup";
        }
        //System.out.println(authentication.getName());
        if(authentication.isAuthenticated()) {
            Users u = userRepo.findById(authentication.getName()).get();
            //System.out.println(u.getChannel());
            if (u.getChannel() != null) {
                for (Channel channel : new ArrayList<>(u.getChannel())) {
                    System.out.println(channel);
                    //System.out.println(channel);
                    simpMessagingTemplate.convertAndSend("/topic/exit" + channel.getChannelname(), authentication.getName() + " has left the channel.");
                    u.leaveChannel(channel);  // safely modifies the original collection
                    //System.out.println("delete");
                }
                userRepo.save(u); // Save after loop
            }
            System.out.println(u.getChannel());
            model.addAttribute("host",authentication.getName());
            model.addAttribute("list",channelRepo.findAllByHost(authentication.getName()));
            
            return "AfterLogin";
        }
        return "LoginAndSignup";
    }

    @PostMapping("/newuser")
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody Users user) {
        Map<String, Object> response = new HashMap<>();
        if (userRepo.existsById(user.getId())) {
            response.put("success", false);
            response.put("message", "User already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            response.put("success", true);
        }
        return response;
    }

    @PostMapping("/create")
    public String createChannel(@RequestParam String channelname,
                                @RequestParam String password,
                                Authentication authentication, Model model) {

        if (channelname == null || channelname.isEmpty()) {
            throw new IllegalArgumentException("Channel name is required");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if(channelRepo.existsById(channelname)){
            model.addAttribute("already","Room Already exists");
            return "redirect:/home";
        }
        System.out.println(channelname+" "+password);
        String host = authentication.getName();
        Channel channel = new Channel(channelname, host, passwordEncoder.encode(password));
        channelRepo.save(channel);

        return "redirect:/home"; // or another page
    }

    @PostMapping("/join")
    public String joinChannel(@RequestParam String channelname,
                              @RequestParam String password,
                              Authentication authentication, Model model) {

        Users user = userRepo.findById(authentication.getName()).orElseThrow();
        Channel channel = channelRepo.findById(channelname).orElseThrow();
        if(!channelRepo.existsById(channelname)){
            model.addAttribute("already","Room Not exists");
            return "redirect:/home";
        }
        if (!passwordEncoder.matches(password, channel.getPassword())) {
            model.addAttribute("already","Invalid Password");
            return "AfterLogin"; // Invalid password
        }

        if (!user.getChannel().contains(channel)) {
            // ✅ Add both sides
            user.getChannel().add(channel);
            channel.getUsers().add(user);

            // ✅ Save (usually only user is enough)
            userRepo.save(user);
        } else {
            //System.out.println("User already in channel");
            return "already";
        }
        chatController.joinMessage(channelname,authentication.getName());
        model.addAttribute("channelName", channelname);
        model.addAttribute("userName", authentication.getName());
        return "home";
    }



    @PostMapping("/exit")
    public String exit(Authentication authentication,
                       @RequestParam("channelname") String name) {
        //System.out.println(name);
        Users user = userRepo.findById(authentication.getName()).orElseThrow();
        Channel channel = channelRepo.findById(name).orElseThrow();

        user.leaveChannel(channel);      // update both sides
        userRepo.save(user);             // save only owning side
        simpMessagingTemplate.convertAndSend("/topic/exit" + name, authentication.getName() + " has left the channel.");
        return "LoginAndSignup";
    }

    @PostMapping("/chat")
    public String chat(Model model,Authentication authentication){
        model.addAttribute("name", authentication.getName());
        model.addAttribute("users",userRepo.findAll());
        System.out.println("chat");
        return "chats";
    }

    @PostMapping("/deleteChannel")
    public ResponseEntity<String> deleteChannel(@RequestBody Map<String, String> payload, Authentication authentication) {
        String optionalChannel = payload.get("channelname");
        System.out.println(optionalChannel);
        channelRepo.deleteById(optionalChannel);
        return ResponseEntity.ok("Channel deleted");
    }

    // @PostMapping("/share")
    // public String shareChannel(@RequestParam("channelname") String name, Model model) {
    //     Channel channel = channelRepo.findById(name).orElseThrow();
    //     model.addAttribute("channel",channel);
    //     return "join";
    // }

    // @PostMapping("/joinChannel")
    // public String Sendmail(@RequestParam("email") String email,
    //                        @RequestParam("channelname") String channelname,
    //                        Model model) {
    // Channel channel = channelRepo.findById(channelname).orElseThrow();
    // emailService.sendMail(channel,email);
    // System.out.println("mail sent");
    // return "LoginAndSignup";    
    // }
}
