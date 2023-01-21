package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/data.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaStorageTest {
    private final MpaStorage mpaStorage;
    private final Mpa mpa1 = getMpa(1, "G");
    private final Mpa mpa2 = getMpa(2, "PG");

    @Test
    public void getAll() {
        assertThat(mpaStorage.getAll()).hasSize(5);
    }

    @Test
    public void getById() {
        assertThat(mpaStorage.getById(1)).isEqualTo(mpa1);
        assertThat(mpaStorage.getById(2)).isEqualTo(mpa2);
    }

    private Mpa getMpa(Integer id, String name) {
        return new Mpa(id, name);
    }
}
