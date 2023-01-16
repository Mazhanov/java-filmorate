package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(int id) {
        checkingPresenceMpa(id);
        return mpaStorage.getById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    private void checkingPresenceMpa(Integer mpaId) {
        if (mpaStorage.getById(mpaId) != null) {
            return;
        }
        throw new ObjectNotFoundException("Рейтинг с id" + mpaId + " не найден");
    }
}
