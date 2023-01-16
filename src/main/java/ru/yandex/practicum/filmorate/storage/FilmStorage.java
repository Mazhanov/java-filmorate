package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film getFilm(int id);
    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void removeFilm(Film film);

    void addLikeFilm(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilm(int count);
}
