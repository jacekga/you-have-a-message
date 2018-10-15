package com.youhaveamessage.model;

public class UserMessagePair {

    private final String userName;
    private final Message message;

    public UserMessagePair(String userName, Message message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "UserMessagePair{" +
                "userName='" + userName + '\'' +
                ", message=" + message +
                '}';
    }
}
