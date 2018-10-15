package com.youhaveamessage.repository;

import com.youhaveamessage.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserMessageRepository implements MessageRepository {

    private final Map<String, List<Message>> userMessagesMap = new HashMap<>();

    @Override
    public boolean addFirstUserMessage(String userName, Message message) {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        userMessagesMap.put(userName, messages);
        return true;
    }

    @Override
    public List<Message> getUserMessages(final String userName) {
        return userMessagesMap.get(userName);
    }
}
