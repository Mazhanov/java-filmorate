package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    private static final String WHITESPACE = " ";

    @GetMapping
    public Collection<User> getUsers() {
        log.info("запрос GET /users обработан");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        validateWhitespaceLogin(user.getLogin());
        generateId(user);

        if (user.getName() == null || user.getEmail().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Добавлен пользователь " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        validateWhitespaceLogin(user.getLogin());

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновленны данные пользователя " + user);
            return user;
        } else {
            throw new ObjectAlreadyExistException("Пользователя с id " + user.getId() + " не найден");
        }
    }

    private void generateId(User user) {
        id++;
        user.setId(id);
    }

    private void validateWhitespaceLogin(String login) {
        if (login.contains(WHITESPACE)) {
            log.warn("Ошибка Валидации " + login + " содержит пробелы");
            throw new ValidationException("Ошибка Валидации " + login + " содержит пробелы");
        }
    }
}
