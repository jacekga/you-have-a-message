package com.youhaveamessage.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserWithFollowedRepository implements UserRepository{

    private final Map<String, Set<String>> usersWithFollowedMap = new HashMap<>();

    @Override
    public boolean addUser(String userName) {
        if(usersWithFollowedMap.containsKey(userName)){
            return false;
        }
        usersWithFollowedMap.put(userName, new HashSet<>());
        return true;
    }

    @Override
    public boolean startFollowing(final String userName, final String userToFollow) {
        return Optional.ofNullable(usersWithFollowedMap.get(userName))
                .map(followed -> followed.add(userToFollow))
                .orElse(Boolean.FALSE);
    }

    @Override
    public boolean stopFollowing(String userName, String userToFollow) {
        return Optional.ofNullable(usersWithFollowedMap.get(userName))
                .map(followed -> followed.remove(userToFollow))
                .orElse(Boolean.FALSE);
    }

    @Override
    public Set<String> getFollowed(final String userName) {
        return Optional.ofNullable(usersWithFollowedMap.get(userName)).orElseGet(Collections::emptySet);
    }

    @Override
    public boolean userExists(String userName) {
        return usersWithFollowedMap.containsKey(userName);
    }
}
