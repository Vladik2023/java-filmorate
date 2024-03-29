package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Collections;
import ru.practicum.filmorate.model.Genre;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAllGenres() {
        return Collections.unmodifiableCollection(genreStorage.getAllGenres().values());
    }

    public Genre getGenreById(int id) {
        return genreStorage.findGenreById(id).orElseThrow(() -> new NotFoundException("Что то не работает"));
    }
}
