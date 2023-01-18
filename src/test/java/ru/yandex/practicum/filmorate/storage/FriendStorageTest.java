package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM FRIENDSHIP")
@Sql(statements = "DELETE FROM USERS")
@Sql(statements = "ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendStorageTest {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    private final User user1 = getUser(1, "one");
    private final User user2 = getUser(2, "two");
    private final User user3 = getUser(2, "two_two");

    @Test
    public void addFriend() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        assertThat(friendStorage.getFriends(1)).hasSize(0);
        friendStorage.addFriend(user1.getId(), user2.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(1);
    }

    @Test
    public void removeFriend() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        friendStorage.addFriend(user1.getId(), user2.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(1);
        friendStorage.removeFriend(user1.getId(), user2.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(0);
    }

    @Test
    public void getFriends() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        friendStorage.addFriend(user1.getId(), user2.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(1);
        friendStorage.addFriend(user1.getId(), user3.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(2);
        friendStorage.removeFriend(user1.getId(), user2.getId());
        assertThat(friendStorage.getFriends(1)).hasSize(1);
    }

    @Test
    public void getFriendsCommon() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        assertThat(friendStorage.getFriendsCommon(1, 2)).hasSize(0);
        friendStorage.addFriend(user1.getId(), user3.getId());
        friendStorage.addFriend(user2.getId(), user3.getId());
        assertThat(friendStorage.getFriendsCommon(1, 2)).hasSize(1);
    }

    private User getUser(int id, String email) {
        return new User(id, email, "login", "name", LocalDate.EPOCH);
    }
}