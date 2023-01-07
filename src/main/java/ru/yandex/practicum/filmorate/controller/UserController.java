package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        Collection<User> users = userService.getUsers();
        log.info("Возвращен список пользователей");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        User newUser = userService.createUser(user);
        log.info("Добавлен пользователь {}", user);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User updateUser = userService.updateUser(user);
        log.info("Обновлен пользователь {}", user);
        return updateUser;
    }

    @DeleteMapping
    public void removeUser(@Valid @RequestBody User user) {
        userService.removeUser(user);
        log.info("Удален пользователь {}", user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.addFriend(id, friendId);
        log.info("Пользователи добавлены в друзья {} и {}", user, userService.getUser(friendId));
        return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.removeFriend(id, friendId);
        log.info("Пользователи удалены друг у друга из друзей {} и {}", user, userService.getUser(friendId));
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        User user = userService.getUser(id);
        log.info("Возвращен пользователь {}", user);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        List<User> friendsUser = userService.getFriends(id);
        log.info("Возвращен список друзей пользователя {}", userService.getUser(id));
        return friendsUser;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Возвращен список общих друзей пользователей {} и {}", userService.getUser(id), userService.getUser(otherId));
        return commonFriends;
    }
}
