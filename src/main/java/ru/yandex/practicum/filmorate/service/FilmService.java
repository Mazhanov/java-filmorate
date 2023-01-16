package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate MIN_RELEASE_DATA_FILM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilm(int id) {
        checkingPresenceFilm(id);
        return filmStorage.getFilm(id);
    }
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        validateReleaseData(film.getReleaseDate());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        checkingPresenceFilm(film.getId());
        validateReleaseData(film.getReleaseDate());
        return filmStorage.updateFilm(film);
    }

    public void removeFilm(Film film) {
        checkingPresenceFilm(film.getId());
        filmStorage.removeFilm(film);
    }

    public void addLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);
        checkingPresenceUser(userId);
        filmStorage.addLikeFilm(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);
        checkingPresenceUser(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int countFilms) {
        return filmStorage.getPopularFilm(countFilms);
    }

    private void checkingPresenceFilm(Integer filmId) { // Проверка наличия фильма в хранилище
        if (filmStorage.getFilm(filmId) != null) {
            return;
        }
        throw new ObjectNotFoundException("Фильм с id" + filmId + " не найден");
    }

    private void checkingPresenceUser(Integer id) { // Проверка наличия пользователя в хранилище
        if (userStorage.getUser(id) == null) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь с id" + id + " не найден");
        }
    }

    private void validateReleaseData(LocalDate localDate) {
        if (localDate.isBefore(MIN_RELEASE_DATA_FILM)) {
            log.warn("Ошибка Валидации дата {} некорректная", localDate);
            throw new ValidationException(String.format("Ошибка валидации, дата %s некорректная", localDate));
        }
    }
}
