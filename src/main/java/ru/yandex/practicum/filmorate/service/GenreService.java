package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(int id) {
        checkingPresenceGenre(id);
        return genreStorage.getById(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    private void checkingPresenceGenre(Integer genreId) {
        if (genreStorage.getById(genreId) != null) {
            return;
        }
        throw new ObjectNotFoundException("Жанр с id" + genreId + " не найден");
    }
}
