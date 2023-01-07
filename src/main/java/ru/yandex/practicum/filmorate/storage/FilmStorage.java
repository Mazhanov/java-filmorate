package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film getFilm(int id);
    Map<Integer, Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void removeFilm(Film film);
}
