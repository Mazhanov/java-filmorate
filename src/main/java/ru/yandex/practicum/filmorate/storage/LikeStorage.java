package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLikeFilm(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilm(int count);
}
