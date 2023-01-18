package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM GENRE")
@Sql(statements = "ALTER TABLE GENRE ALTER COLUMN GENRE_ID RESTART WITH 1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final GenreStorage genreStorage;
    private final Genre genre1 = getGenre(1);

    @Test
    public void getGenres() {
        assertThat(genreStorage.getAll()).isEmpty();
        genreStorage.createGenre(genre1);
        assertThat(genreStorage.getAll()).hasSize(1);
    }

    @Test
    public void getGenresById() {
        assertThat(genreStorage.getById(1)).isNull();
        genreStorage.createGenre(genre1);
        assertThat(genreStorage.getById(1)).isEqualTo(genre1);
    }

    @Test
    public void createMpa() {
        assertThat(genreStorage.createGenre(genre1)).isEqualTo(genre1);
    }

    private Genre getGenre(Integer id) {
        return new Genre(id, "nameMpa");
    }
}