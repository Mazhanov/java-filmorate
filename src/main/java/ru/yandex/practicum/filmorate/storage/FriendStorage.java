package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(int userId, int otherId);

    void removeFriend(int userId, int otherId);

    List<User> getFriends(int userId);

    List<User> getFriendsCommon (Integer userId, Integer otherId);
}
