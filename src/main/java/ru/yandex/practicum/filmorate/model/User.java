package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Setter
public class User {
    private int id;

    @Email (message = "Некорректный email")
    private String email;

    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
