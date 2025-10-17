package com.example.SpringWebSocket.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringWebSocket.Model.Channel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    // @Autowired
    // private JavaMailSender javaMailSender;
    // @Value("${spring.mail.username}")
    // private String sender;
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // public String sendMail(Channel channel,String email) {
    //     try {
    //         MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    //         MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    //         helper.setFrom(sender);
    //         System.out.println();
    //         System.out.println(channel.getChannelname());
    //         helper.setTo(email);
    //         helper.setSubject("🚀 Your OTP for Quiz Shedder 🎉");
    //         String htmlMsg = "<html>" +
    //                 "<body style='font-family: Arial, sans-serif;'>" +
    //                 "<h2 style='color: #2E86C1;'>Dear " + channel.getChannelname() + ", 👋</h2>" +
    //                 "<p>Welcome to <strong>SRG Quizzz</strong>! We're excited to have you on board.</p>" +
    //                 "<p>🎯 <strong>Your One-Time Password (OTP) is:</strong></p>" +
    //                 "<div style='font-size: 24px; color: #D35400; font-weight: bold; padding: 10px; border: 2px dashed #D35400; display: inline-block;'>"
    //                 +
    //                 passwordEncoder.encode(channel.getPassword()) +
    //                 "</div>" +
    //                 "<p style='margin-top: 20px;'>🔒 <i>Please do not share this OTP with anyone. <br> Valid for 30 sec </i></p>"
    //                 +
    //                 "<p>Thanks & Regards,<br><strong>Team SRG Quizzz</strong></p>" +
    //                 "<img src='https://cdn-icons-png.flaticon.com/512/3135/3135715.png' alt='Quiz Icon' width='100' style='margin-top: 10px;'/>"
    //                 +
    //                 "</body></html>";

    //         helper.setText(htmlMsg, true); // true indicates HTML

    //         javaMailSender.send(mimeMessage);
    //         return "successful";
    //     } catch (MessagingException e) {
    //         e.printStackTrace();
    //         return "not sent";
    //     }
    // }
}
