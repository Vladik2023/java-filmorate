package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.Mpa;
import ru.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAllMpa() {
        return Collections.unmodifiableCollection(mpaStorage.getAllMpa().values());
    }

    public Mpa getMpaById(int id) {
        return mpaStorage.findMpaById(id).orElseThrow(() -> new NotFoundException("Что то не работает"));
    }
}
