package com.example.SpringWebSocket.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Typing {
    private String userName;
    private String channelName;
}
