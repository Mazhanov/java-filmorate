package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Genre {
    private int id;
    @NotBlank
    private String name;
}
