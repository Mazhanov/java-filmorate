package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.UsersAlreadyFriendshipException;
import ru.yandex.practicum.filmorate.exception.UsersNotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final Map<Integer, User> users;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.users = userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }
    public Collection<User> getUsers() {
        log.info("Возвращен список пользователей");
        return users.values();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void removeUser(User user) {
        userStorage.removeUser(user);
    }

    public User addFriend(int userId, int friendId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(friendId);

        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user.getFriends() == null || friend.getFriends() == null) {
            userStorage.getUser(userId).getFriends().add(friendId);
            friend.getFriends().add(userId);
            log.info("Пользователи добавлены в друзья {} и {}", user, friend);
            return user;
        }

        if (!(user.getFriends().contains(friendId))) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            log.info("Пользователи добавлены в друзья {} и {}", user, friend);
            return user;
        } else {
            throw new UsersAlreadyFriendshipException(String.format("Пользователи %s и %s уже в друзях, " +
                            "повторное добавление невозможно", userId, friendId));
        }
    }

    public User removeFriend(Integer userId, Integer friendId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(friendId);

        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            log.info("Пользователи удалены друг у друга из друзей {} и {}", user, friend);
            return user;
        } else {
            throw new UsersNotFriendsException(String.format("Пользователи %s и %s не находятся в друзьях, " +
                            "удаление невозможно", userId, friendId));
        }
    }

    public List<User> getFriends(Integer userId) {
        checkingPresenceUser(userId);

        User user = users.get(userId);
        List<User> friendsUser = new ArrayList<>();

        for (Integer friendId : user.getFriends()) {
            friendsUser.add(users.get(friendId));
        }
        log.info("Возвращен список друзей пользователя {}", users.get(userId));
        return friendsUser;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        checkingPresenceUser(userId);
        checkingPresenceUser(otherId);

        List<User> commonFriends = new ArrayList<>();

        Set<Integer> friendsUser = users.get(userId).getFriends();
        Set<Integer> friendsUserSecond = users.get(otherId).getFriends();

        if (friendsUser == null || friendsUserSecond == null) {
            log.info("Возвращен пустой список общих друзей пользователей {} и {}, " +
                    "так как спсиок друзей у одного из пользователей пуст", users.get(userId), users.get(otherId));
            return commonFriends;
        }

        for (Integer friendUser: friendsUser) {
            for (Integer friendSecondUser : friendsUserSecond) {
                if (friendUser.equals(friendSecondUser)) {
                    commonFriends.add(users.get(friendUser));
                }
            }
        }
        log.info("Возвращен список общих друзей пользователей {} и {}", users.get(userId), users.get(otherId));
        return commonFriends;
    }

    private void checkingPresenceUser(Integer userId) { // Проверка наличия пользователя в хранилище
        if (!(users.containsKey(userId))) {
            throw new ObjectNotFoundException("Пользователь с id" + userId + " не найден");
        }
    }
}
