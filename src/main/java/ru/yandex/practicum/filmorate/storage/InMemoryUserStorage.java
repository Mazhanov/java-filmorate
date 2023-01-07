package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    private void generateId(User user) {
        id++;
        user.setId(id);
    }

    @Override
    public User getUser(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь с id" + id + " не найден");
        }
    }
    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        validateWhitespaceLogin(user.getLogin());
        generateId(user);
        checkNameUser(user);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateWhitespaceLogin(user.getLogin());

        if (users.containsKey(user.getId())) {
            checkNameUser(user);
            users.put(user.getId(), user);
            log.info("Обновлены данные пользовтеля {}", user);
            return user;
        } else {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new ObjectNotFoundException("Пользователь с id" + user.getId() + " не найден");
        }
    }

    @Override
    public void removeUser(User user) {

        if (users.containsValue(user)) {
            users.remove(user.getId());
        } else {
            log.warn("Не найден пользователь {}", user);
            throw new ObjectNotFoundException("Пользователь с id" + user.getId() + " не найден");
        }
    }

    private void validateWhitespaceLogin(String login) {
        if (login.contains(" ")) {
            log.warn("Ошибка Валидации {} содержит пробелы", login);
            throw new ValidationException(String.format("Ошибка Валидации, логин %s содержит пробелы", login));
        }
    }

    private void checkNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}