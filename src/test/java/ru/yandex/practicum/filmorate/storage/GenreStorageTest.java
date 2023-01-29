package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/data.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final GenreStorage genreStorage;
    private final Genre genre1 = getGenre(1, "Комедия");
    private final Genre genre2 = getGenre(2, "Драма");

    @Test
    public void getAll() {
        assertThat(genreStorage.getAll()).hasSize(6);
    }

    @Test
    public void getById() {
        assertThat(genreStorage.getById(1)).isEqualTo(genre1);
        assertThat(genreStorage.getById(2)).isEqualTo(genre2);
    }

    private Genre getGenre(Integer id, String name) {
        return new Genre(id, name);
    }
}
