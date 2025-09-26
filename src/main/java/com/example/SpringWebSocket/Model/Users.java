package com.example.SpringWebSocket.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;

    @ManyToMany
    private List<Channel> channel;

    public void leaveChannel(Channel channel) {
        if (this.channel.contains(channel)) {
            this.channel.remove(channel);
            channel.getUsers().remove(this); // update inverse side
        }
    }

    public Users(String id, String email, String s) {
        this.id=id;
        this.email=email;
        this.password=s;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
