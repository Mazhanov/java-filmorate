package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User getUser(int id);
    List<User> getUsers();
    User createUser(User user);

    User updateUser(User user);

    void removeUser(User user);
}
