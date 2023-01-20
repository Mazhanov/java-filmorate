package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
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
    public Genre createGenre(Genre genre) {
        final String sqlQuery = "insert into GENRE(NAME) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);

        genre.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return genre;
    }

    @Override
    public Film addGenreForFilm(Film film) {
        final String sqlQuery = "select fg.GENRE_ID, g.NAME " +
                "from FILM_GENRE as fg " +
                "join GENRE as g ON fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID = ?" +
                "order by g.GENRE_ID";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), film.getId());
        Set<Genre> genresSet = new HashSet<>(genres);
        film.setGenres(genresSet);
        return film;
    }

    @Override
    public List<Film> addGenreForListFilm(List<Film> films) {

        Map<Integer, Film> filmsMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));

        String inSql = String.join(", ", Collections.nCopies(filmsMap.size(), "?"));

        final String sqlQuery = "select * " +
                "from FILM_GENRE as fg " +
                "left outer join GENRE as g on fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            filmsMap.get(rs.getInt("FILM_ID")).addGenre(makeGenre(rs));
        }, filmsMap.keySet().toArray());

        return new ArrayList<>(filmsMap.values());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE.NAME")
        );
    }
}
