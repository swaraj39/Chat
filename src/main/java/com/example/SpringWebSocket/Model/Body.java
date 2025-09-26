package com.example.SpringWebSocket.Model;

public class Body {
    private String from;
    private String content;
    private String channelName;

    public Body() {
    }

    public Body(String from, String content) {
        this.from = from;
        this.content = content;
    }

    // getters and setters

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
