package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM FILM")
@Sql(statements = "ALTER TABLE FILM ALTER COLUMN FILM_ID RESTART WITH 1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmStorage filmStorage;
    private final Film film1 = getFilm(1, "one");
    private final Film film2 = getFilm(2, "two");
    private final Film updateFilm = getFilm(2, "two_two");

    @Test
    public void getFilms() {
        assertThat(filmStorage.getFilms()).isEmpty();
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        assertThat(filmStorage.getFilms()).hasSize(2);
    }

    @Test
    public void getFilmById() {
        assertThat(filmStorage.getFilm(1)).isNull();
        filmStorage.createFilm(film1);
        assertThat(filmStorage.getFilm(1)).isEqualTo(film1);
    }

    @Test
    public void createFilm() {
        assertThat(filmStorage.createFilm(film1)).isEqualTo(film1);
        assertThat(filmStorage.createFilm(film2)).isEqualTo(film2);
    }

    @Test
    public void updateFilm() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        assertThat(filmStorage.updateFilm(updateFilm)).isEqualTo(updateFilm);
    }

    @Test
    public void removeFilm() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        assertThat(filmStorage.getFilms()).hasSize(2);
        filmStorage.removeFilm(film2);
        assertThat(filmStorage.getFilms()).hasSize(1);
    }

    private Film getFilm(int id, String email) {
        return new Film(id, email, "description", LocalDate.EPOCH, 100, getMpa());
    }
    private Mpa getMpa() {
        return new Mpa(1, "G");
    }
}