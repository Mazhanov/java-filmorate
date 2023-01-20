package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();
    Genre getById(int id);
    Genre createGenre(Genre genre);
    Film addGenreForFilm(Film film);
    List<Film> addGenreForListFilm(List<Film> films);
}
