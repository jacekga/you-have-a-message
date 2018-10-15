package com.youhaveamessage.repository;

import java.util.Set;

public interface UserRepository {

    boolean addUser(String userName);
    boolean startFollowing(String userName, String userToFollow);
    boolean stopFollowing(String userName, String userToFollow);
    Set<String> getFollowed(String userName);
    boolean userExists(String userName);
}
