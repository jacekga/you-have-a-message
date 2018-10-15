package com.youhaveamessage.repository;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserWithFollowedRepositoryTest {

    private final UserWithFollowedRepository repository = new UserWithFollowedRepository();

    @Test
    void shouldNotAddUserIfUserExists() {
        String username = "myUser";

        repository.addUser(username);

        assertFalse(repository.addUser(username));
    }

    @Test
    void shouldAddUserWithEmptyFollowedSet() {
        String userName = "myUser";

        assertTrue(repository.addUser(userName));
        assertTrue(repository.userExists(userName));
        assertEquals(0, repository.getFollowed(userName).size());
    }

    @Test
    void shouldStartFollowingAnotherUser() {
        String userName = "myUser";
        String userToFollow = "anotherUser";

        repository.addUser(userName);

        assertTrue(repository.startFollowing(userName, userToFollow));
        assertTrue(repository.getFollowed(userName).contains(userToFollow));
    }

    @Test
    void shouldNotAddUserToFollowedTwice() {
        String userName = "myUser";
        String userToFollow = "anotherUser";

        repository.addUser(userName);
        repository.startFollowing(userName, userToFollow);
        repository.startFollowing(userName, userToFollow);

        assertEquals(1, repository.getFollowed(userName).size());
    }

    @Test
    void shouldStopFollowingUser() {
        String userName = "myUser";
        String firstUserToFollow = "firstUser";
        String secondUserToFollow = "secondUser";

        repository.addUser(userName);
        repository.startFollowing(userName, firstUserToFollow);
        repository.startFollowing(userName, secondUserToFollow);
        repository.stopFollowing(userName, firstUserToFollow);

        Set<String> followedUsers = repository.getFollowed(userName);

        assertTrue(followedUsers.contains(secondUserToFollow));
        assertEquals(1, followedUsers.size());
    }

}
