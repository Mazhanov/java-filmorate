package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(int id);
    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void removeFilm(Film film);
}
