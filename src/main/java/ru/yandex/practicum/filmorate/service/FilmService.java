package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private static final LocalDate MIN_RELEASE_DATA_FILM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
    }

    public Film getFilm(int id) {
        checkingPresenceFilm(id);
        Film film = filmStorage.getFilm(id);
        Film filmWithGenre = addGenre(film);
        return filmWithGenre;
    }
    public Collection<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        List<Film> filmsWithGenre = new ArrayList<>();

        for (Film film : films) {
            filmsWithGenre.add(addGenre(film));
        }

        return filmsWithGenre;
    }

    public Film createFilm(Film film) {
        validateReleaseData(film.getReleaseDate());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        checkingPresenceFilm(film.getId());
        validateReleaseData(film.getReleaseDate());
        filmStorage.updateFilm(film);
        Film filmWithGenre = addGenre(film);
        return filmWithGenre;
    }

    public void removeFilm(Film film) {
        checkingPresenceFilm(film.getId());
        filmStorage.removeFilm(film);
    }

    public void addLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);
        checkingPresenceUser(userId);
        likeStorage.addLikeFilm(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);
        checkingPresenceUser(userId);
        likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int countFilms) {
        List<Film> films = likeStorage.getPopularFilm(countFilms);
        List<Film> filmsWithGenre = new ArrayList<>();

        for (Film film : films) {
            filmsWithGenre.add(addGenre(film));
        }
        return filmsWithGenre;
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

    private Film addGenre(Film film) {
        return genreStorage.addGenreForFilm(film);
    }
}
