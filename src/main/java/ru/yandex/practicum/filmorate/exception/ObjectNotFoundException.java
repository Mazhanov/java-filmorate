package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ObjectNotFoundException extends ResponseStatusException {
    public ObjectNotFoundException(HttpStatus status) {
        super(status);
    }
}
