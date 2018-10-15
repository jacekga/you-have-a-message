package com.youhaveamessage.repository;

import com.youhaveamessage.model.Message;

import java.util.List;

public interface MessageRepository {

    boolean addFirstUserMessage(String userName, Message message);
    List<Message> getUserMessages(String userName);
}
