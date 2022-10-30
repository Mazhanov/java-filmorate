package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("запрос GET /films обработан");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        checkValidation(film);
        generateId(film);

        films.put(film.getId(), film);
        log.debug("запрос POST /films обработан, фильм добавлен");
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        checkValidation(film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("запрос PUT /users обработан, фильм обновлен");
            return film;
        } else {
            throw new ObjectAlreadyExistException("Фильм с id " + film.getId() + " не найден");
        }
    }

    private void generateId(Film film) {
        id++;
        film.setId(id);
    }


    private void checkValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка Валидации " + film.getName() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + film.getName() + " некорректный");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка Валидации " + film.getDescription() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + film.getDescription() + " больше 200символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.warn("Ошибка Валидации " + film.getReleaseDate() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + film.getReleaseDate() + " слишком рано");
        }
        if (film.getDuration().getSeconds() < 0) {
            log.warn("Ошибка Валидации " + film.getDuration() + " некорректный");
            throw new ValidationException("Ошибка Валидации " + film.getDuration()
                    + " продолжительность фильма должна быть положительной");
        }
    }
}
