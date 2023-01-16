package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.UsersAlreadyFriendshipException;
import ru.yandex.practicum.filmorate.exception.UsersNotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(int id) {
        checkingPresenceUser(id);
        return userStorage.getUser(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        validateWhitespaceLogin(user);
        checkNameUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        checkingPresenceUser(user.getId());
        validateWhitespaceLogin(user);
        checkNameUser(user);
        return userStorage.updateUser(user);
    }

    public void removeUser(User user) {
        checkingPresenceUser(user.getId());
        userStorage.removeUser(user);
    }

    public void addFriend(int userId, int friendId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(friendId);

        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(friendId);

        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        checkingPresenceUser(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getFriendsCommon(Integer userId, Integer otherId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(otherId);

        return userStorage.getFriendsCommon(userId, otherId);
    }

    private void checkingPresenceUser(Integer id) { // Проверка наличия пользователя в хранилище
        if (userStorage.getUser(id) == null) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь с id" + id + " не найден");
        }
    }

    private boolean friendshipCheck(Integer userId, Integer friendId) {
        return userStorage.getUser(userId).getFriends().contains(friendId);
    }

    private void validateWhitespaceLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка Валидации, логин {} содержит пробелы", user.getLogin());
            throw new ValidationException(String.format("Ошибка Валидации, логин %s содержит пробелы", user.getLogin()));
        }
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
