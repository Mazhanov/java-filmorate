package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getFilm(id);
        log.info("Возвращен фильм {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        Collection<Film> films = filmService.getFilms();
        log.info("Возвращен список всех фильмов {}", films);
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        Film newFilm = filmService.createFilm(film);
        log.info("Добавлен фильм {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updateFilm = filmService.updateFilm(film);
        log.info("Обновлен фильм {}", film);
        return updateFilm;
    }

    @DeleteMapping
    public void removeFilm(@Valid @RequestBody Film film) {
        filmService.removeFilm(film);
        log.info("Удален фильм {}", film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.info("Пользователь {} лайкнул фильм {}", userId, filmService.getFilm(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("Лайк пользователя {} удален с фильма {}", userId, filmService.getFilm(id));
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> topFilms = filmService.getTopFilms(count);
        log.info("Возвращен список популярных фильмов {}", topFilms);
        return topFilms;
    }
}
