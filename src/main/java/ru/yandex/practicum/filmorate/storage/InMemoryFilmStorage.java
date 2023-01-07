package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATA_FILM = LocalDate.of(1895, 12, 28);

    private void generateId(Film film) {
        id++;
        film.setId(id);
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ObjectNotFoundException("Фильм с id" + id + " не найден");
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        validateReleaseData(film.getReleaseDate());
        generateId(film);

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateReleaseData(film.getReleaseDate());

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new ObjectNotFoundException("Фильм с id" + film.getId() + " не найден");
        }
    }

    @Override
    public void removeFilm(Film film) {
        if (films.containsValue(film)) {
            log.info("Удален фильм {}", film);
            films.remove(film.getId());
        } else {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new ObjectNotFoundException("Фильм с id" + film.getId() + " не найден");
        }
    }

    private void validateReleaseData(LocalDate localDate) {
        if (localDate.isBefore(MIN_RELEASE_DATA_FILM)) {
            log.warn("Ошибка Валидации дата {} некорректная", localDate);
            throw new ValidationException(String.format("Ошибка валидации, дата %s некорректная", localDate));
        }
    }
}
