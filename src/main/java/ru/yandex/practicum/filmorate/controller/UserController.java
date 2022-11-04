package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("запрос GET /users обработан");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateWhitespaceLogin(user.getLogin());
        generateId(user);
        checkNameUser(user);

        users.put(user.getId(), user);
        log.info("Добавлен пользователь { }" + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateWhitespaceLogin(user.getLogin());

        if (users.containsKey(user.getId())) {
            checkNameUser(user);
            users.put(user.getId(), user);
            log.info("Обновленны данные пользователя { }" + user);
            return user;
        } else {
            log.warn("Пользователя с id " + user.getId() + " не найден");
            throw new ObjectAlreadyExistException(HttpStatus.NOT_FOUND);
        }
    }

    private void generateId(User user) {
        id++;
        user.setId(id);
    }

    private void validateWhitespaceLogin(String login) {
        if (login.contains(" ")) {
            log.warn("Ошибка Валидации " + login + " содержит пробелы");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
