package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Service
@Slf4j
public class MpaDBStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(int id) {
        final String sqlQuery = "select * from MPA where MPA_ID = ?";
        final List<Mpa> mpas = jdbcTemplate.query(sqlQuery, this::makeMpa, id);
        if (mpas.isEmpty()) {
            return null;
        }
        return mpas.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "select * from MPA order by MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa createMpa(Mpa mpa) {
        final String sqlQuery = "insert into MPA(NAME) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"MPA_ID"});
            stmt.setString(1, mpa.getName());
            return stmt;
        }, keyHolder);

        mpa.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return mpa;
    }

    private Mpa makeMpa(ResultSet rs, int id) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("NAME")
        );
    }
}