package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDBStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getByFilmId(int id) {
        final String sqlQuery = "select fg.GENRE_ID, g.NAME " +
                "from FILM_GENRE as fg " +
                "join GENRE as g ON fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID = ?" +
                "order by g.GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, id);
    }

    @Override
    public List<Genre> getAll() {
        final String sqlQuery = "select * from GENRE order by GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getById(int id) {
        final String sqlQuery = "select * from GENRE where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genres.isEmpty()) {
            return null;
        }
        return genres.get(0);
    }

    private Genre makeGenre(ResultSet rs, int id) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE.NAME")
        );
    }
}
