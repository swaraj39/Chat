package com.example.SpringWebSocket.Controllers;

import com.example.SpringWebSocket.Model.Channel;
import com.example.SpringWebSocket.Model.Users;
import com.example.SpringWebSocket.Repository.ChannelRepo;
import com.example.SpringWebSocket.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class FrontController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ChannelRepo channelRepo;

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
            System.out.println("no");
            return "LonginAndSignup";
        }
        System.out.println(authentication.getName());
        if(authentication.isAuthenticated()) {
            Users u = userRepo.findById(authentication.getName()).get();
            //System.out.println(u.getChannel());
            if (u.getChannel() != null) {
                for (Channel channel : new ArrayList<>(u.getChannel())) {
                    System.out.println(channel);
                    u.leaveChannel(channel);  // safely modifies the original collection
                    System.out.println("delete");
                }
                userRepo.save(u); // Save after loop
            }
            //System.out.println(u.getChannel());
            model.addAttribute("host",authentication.getName());
            return "AfterLogin";
        }
        return "LoginAndSignup";
    }

    @RequestMapping("/newuser")
    @ResponseBody
    public String newuser(@ModelAttribute Users users) {
        String s = passwordEncoder.encode(users.getPassword());
        Users u = new Users(users.getId(), users.getEmail(), s);
        System.out.println(u);
        userRepo.save(u);
        System.out.println("Saved");
        return "Added";
    }

    @PostMapping("/create")
    public String createChannel(@RequestParam String channelname,
                                @RequestParam String password,
                                Authentication authentication) {

        if (channelname == null || channelname.isEmpty()) {
            throw new IllegalArgumentException("Channel name is required");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

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

        if (!passwordEncoder.matches(password, channel.getPassword())) {
            return "LoginAndSignup"; // Invalid password
        }

        if (!user.getChannel().contains(channel)) {
            // ✅ Add both sides
            user.getChannel().add(channel);
            channel.getUsers().add(user);

            // ✅ Save (usually only user is enough)
            userRepo.save(user);
        } else {
            System.out.println("User already in channel");
            return "already";
        }

        model.addAttribute("channelName", channelname);
        model.addAttribute("userName", authentication.getName());
        return "home";
    }



    @PostMapping("/exit")
    public String exit(Authentication authentication,
                       @RequestParam("channelname") String name) {

        Users user = userRepo.findById(authentication.getName()).orElseThrow();
        Channel channel = channelRepo.findById(name).orElseThrow();

        user.leaveChannel(channel);      // update both sides
        userRepo.save(user);             // save only owning side

        return "LoginAndSignup";
    }


}
