package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User getUser(int id);
    List<User> getUsers();
    User createUser(User user);

    User updateUser(User user);

    void removeUser(User user);

    void addFriend(int userId, int otherId);

    void removeFriend(int userId, int otherId);

    List<User> getFriends(int userId);

    List<User> getFriendsCommon (Integer userId, Integer otherId);
}
