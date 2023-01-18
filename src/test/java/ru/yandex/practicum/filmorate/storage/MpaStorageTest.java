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
@Sql(statements = "DELETE FROM FILM")
@Sql(statements = "DELETE FROM MPA")
@Sql(statements = "ALTER TABLE MPA ALTER COLUMN MPA_ID RESTART WITH 1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaStorageTest {
    private final MpaStorage mpaStorage;
    private final Mpa mpa1 = getMpa(1);

    @Test
    public void getMpas() {
        assertThat(mpaStorage.getAll()).isEmpty();
        mpaStorage.createMpa(mpa1);
        assertThat(mpaStorage.getAll()).hasSize(1);
    }

    @Test
    public void getMpaById() {
        assertThat(mpaStorage.getById(1)).isNull();
        mpaStorage.createMpa(mpa1);
        assertThat(mpaStorage.getById(1)).isEqualTo(mpa1);
    }

    @Test
    public void createMpa() {
        assertThat(mpaStorage.createMpa(mpa1)).isEqualTo(mpa1);
    }

    private Mpa getMpa(Integer id) {
        return new Mpa(id, "nameMpa");
    }
}