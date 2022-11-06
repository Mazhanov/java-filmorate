package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATA_FILM = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("запрос GET /films обработан");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateReleaseData(film.getReleaseDate());
        generateId(film);

        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateReleaseData(film.getReleaseDate());

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм {}", film);
            return film;
        } else {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new ObjectNotFoundException(HttpStatus.NOT_FOUND);
        }
    }

    private void generateId(Film film) {
        id++;
        film.setId(id);
    }

    private void validateReleaseData(LocalDate localDate) {
        if (localDate.isBefore(MIN_RELEASE_DATA_FILM)) {
            log.warn("Ошибка Валидации дата {} некорректная", localDate);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
