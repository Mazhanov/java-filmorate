package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

public class ObjectAlreadyExistException extends ResponseStatusException {
    public ObjectAlreadyExistException(HttpStatus status) {
        super(status);
    }
}
