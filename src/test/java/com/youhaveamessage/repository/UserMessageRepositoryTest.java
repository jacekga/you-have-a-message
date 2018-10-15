package com.youhaveamessage.repository;

import com.youhaveamessage.model.Message;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMessageRepositoryTest {

    private final UserMessageRepository messageRepository = new UserMessageRepository();

    @Test
    void shouldReturnNullWhenUserNotFound(){
        assertNull(messageRepository.getUserMessages("SomeUser"));
    }

    @Test
    void shouldAddFirstUserWithMessage(){
        String firstMessage = "First message";
        String firstUser = "FirstUser";

        boolean result = messageRepository.addFirstUserMessage(firstUser, new Message(Instant.now(), firstMessage));
        List<Message> messages = messageRepository.getUserMessages(firstUser);

        assertTrue(result);
        assertEquals(1, messages.size());
        assertNotNull(messages.get(0).getInstant());
        assertEquals(firstMessage, messages.get(0).getMessage());
    }
}
