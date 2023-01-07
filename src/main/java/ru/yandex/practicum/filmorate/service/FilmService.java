package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }
    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void removeFilm(Film film) {
        filmStorage.removeFilm(film);
    }

    public void addLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);
        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        checkingPresenceFilm(filmId);

        Film film = filmStorage.getFilm(filmId);

        if (film.getLikes() == null) {
            throw new ObjectNotFoundException("Список лайков пустой, невозможно удалить лайк");
        }

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователь с id %s не ставил лайк фильму с id %s",
                    userId, filmId));
        }
    }

    public List<Film> getTopFilms(int countFilms) {
        return filmStorage.getFilms().values().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private void checkingPresenceFilm(Integer filmId) { // Проверка наличия фильма в хранилище
        if (filmStorage.getFilms().containsKey(filmId)) {
            return;
        }
        throw new ObjectNotFoundException("Фильм с id" + filmId + " не найден");
    }
}
