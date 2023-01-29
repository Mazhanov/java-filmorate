package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Service
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikeFilm(int filmId, int userId) {
        String sqlQuery = "insert into LIKES(FILM_ID, USER_ID)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES " +
                "where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilm(int count) {
        final String sqlQuery = "select *, COUNT(l.FILM_ID) AS likes " +
                "from FILM as f " +
                "join MPA AS m ON f.MPA_ID = m.MPA_ID " +
                "left join LIKES AS l ON f.FILM_ID = l.FILM_ID " +
                "group by f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_ID, m.NAME " +
                "order by likes desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet rs, int id) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA.NAME"))
        );
    }
}
