package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Service
@Slf4j
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage (JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film getFilm(int id) {
        final String sqlQuery = "select * " +
                "from FILM as f join MPA AS m ON f.MPA_ID = m.MPA_ID " +
                "where film_id = ?";
        final List<Film> film = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (film.isEmpty()) {
            return null;
        }
        return film.get(0);
    }

    @Override
    public List<Film> getFilms() {
        final String sqlQuery = "select * " +
                "from FILM as f join MPA AS m ON f.MPA_ID = m.MPA_ID ";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film createFilm(Film film) {
        final String sqlQuery = "insert into FILM(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        updateFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILM set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "where FILM_ID = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (changes == 1) {
            updateFilmGenre(film);
            return getFilm(film.getId());
        }
        return null;
    }

    @Override
    public void removeFilm(Film film) {
        String sqlQuery = "delete from FILM where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
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

    private void updateFilmGenre(Film film) { // Обновление жарнов фильма
        String sqlQueryDeleteGenre = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteGenre, film.getId());

        if(film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        Set<Genre> genres = new HashSet<>(film.getGenres());

        for (Genre genre : genres) {
            String sqlQueryAddGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryAddGenre, film.getId(), genre.getId());
        }
    }

    private Film makeFilm(ResultSet rs, int id) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA.NAME")),
                genreStorage.getByFilmId(rs.getInt("FILM_ID"))
        );
    }
}
