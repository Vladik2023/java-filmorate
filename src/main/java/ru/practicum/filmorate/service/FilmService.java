package ru.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(int id, Film updatedFilm) {
        films.put(id, updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}