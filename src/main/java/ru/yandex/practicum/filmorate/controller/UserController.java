package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("запрос GET /users обработан");
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        checkValidation(user);
        generateId(user);

        if (user.getName() == null || user.getEmail().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.debug("запрос POST /users обработан, пользователь добавлен");
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        checkValidation(user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("запрос PUT /users обработан, пользователь обновлен");
            return user;
        } else {
            throw new ObjectAlreadyExistException("Пользователя с id " + user.getId() + " не найден");
        }
    }

    private void generateId(User user) {
        id++;
        user.setId(id);
    }

    private void checkValidation (User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.warn("Ошибка Валидации " + user.getEmail() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + user.getEmail() + " некорректный");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || (user.getLogin().contains(" "))) {
            log.warn("Ошибка Валидации " + user.getLogin() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + user.getLogin() + " некорректный");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка Валидации " + user.getBirthday() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + user.getBirthday() + " дата рождения не может быть в будущем");
        }
    }
}
