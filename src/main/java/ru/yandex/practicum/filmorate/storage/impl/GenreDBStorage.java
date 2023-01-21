package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        final String sqlQuery = "select * from GENRE order by GENRE_ID";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getById(int id) {
        final String sqlQuery = "select * from GENRE where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        if (genres.isEmpty()) {
            return null;
        }
        return genres.get(0);
    }

    @Override
    public void addGenreForListFilm(List<Film> films) {
        Map<Integer, Film> filmsMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));

        String inSql = String.join(", ", Collections.nCopies(filmsMap.size(), "?"));

        final String sqlQuery = "select * " +
                "from FILM_GENRE as fg " +
                "left outer join GENRE as g on fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID in (" + inSql + ") " +
                "order by fg.GENRE_ID";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            filmsMap.get(rs.getInt("FILM_ID")).addGenre(makeGenre(rs));
        }, filmsMap.keySet().toArray());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE.NAME")
        );
    }
}
