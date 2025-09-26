package com.example.SpringWebSocket.Repository;

import com.example.SpringWebSocket.Model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepo extends JpaRepository<Channel,String> {
}
