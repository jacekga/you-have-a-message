package com.youhaveamessage.services;

import com.google.common.collect.Lists;
import com.youhaveamessage.model.Message;
import com.youhaveamessage.model.UserMessagePair;
import com.youhaveamessage.repository.MessageRepository;
import com.youhaveamessage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserMessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }
    
    public boolean addUserMessage(final String userName, final Message message) {
        return Optional.ofNullable(messageRepository.getUserMessages(userName))
                .map(messages -> messages.add(message))
                .orElseGet(() -> createUserWithFirstMessage(userName, message));
    }
    
    public List<Message> getMessagesFromLatest(final String userName){
        return Lists.reverse(Optional.ofNullable(messageRepository.getUserMessages(userName)).orElseGet(Collections::emptyList));
    }
    
    public boolean startFollowing(final String userName, final String userToFollow){
        return userRepository.startFollowing(userName, userToFollow);
    }
    
    public boolean stopFollowing(final String userName, final String userToFollow) {
        return userRepository.stopFollowing(userName,userToFollow);
    }
    
    public List<UserMessagePair> getTimeline(final String userName) {
        final Set<String> followedUsers = Optional.ofNullable(userRepository.getFollowed(userName))
                .orElseGet(Collections::emptySet);

        return followedUsers.stream().map(this::getUserMessagePairs)
                .flatMap(Function.identity())
                .sorted(Comparator.comparing(UserMessageService::getInstantFromMessage).reversed())
                .collect(Collectors.toList());
    }

    public boolean userExists(final String userName){
        return userRepository.userExists(userName);
    }

    private synchronized boolean createUserWithFirstMessage(final String userName, final Message message) {
        if(messageRepository.addFirstUserMessage(userName, message)){
            return userRepository.addUser(userName);
        }
        return false;
    }

    private Stream<UserMessagePair> getUserMessagePairs(String followedUserName){
        return Optional.ofNullable(messageRepository.getUserMessages(followedUserName)).orElseGet(Collections::emptyList).stream()
                .map(message -> new UserMessagePair(followedUserName, message));
    }

    private static Instant getInstantFromMessage(UserMessagePair pair) {
        return pair.getMessage().getInstant();
    }
}
