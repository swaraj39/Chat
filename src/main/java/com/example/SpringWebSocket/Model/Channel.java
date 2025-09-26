package com.example.SpringWebSocket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
public class Channel {

    @Id
    @Column(name = "channelname", nullable = false)
    private String channelname;
    @Column(name = "host", nullable = false)
    private String host;
    @Column(name = "password",nullable = false)
    private String password;

    @ManyToMany(mappedBy = "channel")
    private List<Users> users;
    public Channel() {
    }

    public Channel(String channelname, String host, String encode) {
        this.channelname = channelname;
        this.host = host;
        this.password = encode;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelname='" + channelname + '\'' +
                ", host='" + host + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
