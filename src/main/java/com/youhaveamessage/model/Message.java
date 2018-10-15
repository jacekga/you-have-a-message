package com.youhaveamessage.model;

import java.time.Instant;

public class Message {

    private Instant instant;
    private String message;

    public Message(){
        this.instant = Instant.now();
    }

    public Message(Instant instant, String message){
        this.instant = instant;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getInstant() {
        return instant;
    }

    @Override
    public String toString() {
        return "Message{" +
                "instant=" + instant +
                ", message='" + message + '\'' +
                '}';
    }

    private void setInstant(Instant instant) {}
}
