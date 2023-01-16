package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = mpaService.getAllMpa();
        log.info("Возвращены все рейтинги");
        return mpas;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        Mpa mpa = mpaService.getMpaById(id);
        log.info("Возвращен рейтинг по id {}", id);
        return mpa;
    }
}
