package ru.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.utils.Validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            Validators.logAndError("Ошибка! Невозможно обновить фильм - его не существует.");
        }
        validate(film);
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void validate(Film film) {
        Validators.validateName(film.getName());
        Validators.validateDescription(film.getDescription());
        Validators.validateReleaseDate(film.getReleaseDate());
        Validators.validateDuration(film.getDuration());
        validateId(film);
    }

    public void validateId(Film film) {
        if (film.getId() == 0) {
            film.setId(Film.filmsId++);
        }
    }
    public Film getFilmById(int id) {
        for (Film film : films.values()) {
            if (film.getId() == id) {
                return film;
            }
        }
        return null;
    }
}
