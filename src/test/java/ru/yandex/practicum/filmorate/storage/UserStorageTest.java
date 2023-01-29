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
@Sql({"/schema.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageTest {
    private final UserStorage userStorage;
    private final User user1 = getUser(1, "one");
    private final User user2 = getUser(2, "two");
    private final User updateUser = getUser(2, "two_two");

    @Test
    public void getUsers() {
        assertThat(userStorage.getUsers()).isEmpty();
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        assertThat(userStorage.getUsers()).hasSize(2);
    }

    @Test
    public void getUserById() {
        assertThat(userStorage.getUser(1)).isNull();
        userStorage.createUser(user1);
        assertThat(userStorage.getUser(1)).isEqualTo(user1);
    }

    @Test
    public void createUser() {
        assertThat(userStorage.createUser(user1)).isEqualTo(user1);
        assertThat(userStorage.createUser(user2)).isEqualTo(user2);
    }

    @Test
    public void updateUser() {
        assertThat(userStorage.createUser(user1)).isEqualTo(user1);
        assertThat(userStorage.createUser(user2)).isEqualTo(user2);
        assertThat(userStorage.updateUser(updateUser)).isEqualTo(updateUser);
    }

    @Test
    public void removeUser() {
        assertThat(userStorage.createUser(user1)).isEqualTo(user1);
        assertThat(userStorage.createUser(user2)).isEqualTo(user2);
        assertThat(userStorage.getUsers()).hasSize(2);
        userStorage.removeUser(user2);
        assertThat(userStorage.getUsers()).hasSize(1);
    }

    private User getUser(int id, String email) {
        return new User(id, email, "login", "name", LocalDate.EPOCH);
    }
}