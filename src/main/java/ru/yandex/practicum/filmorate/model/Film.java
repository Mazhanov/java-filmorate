package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotEmpty
    private String name;

    @Max(200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private long duration;
}
