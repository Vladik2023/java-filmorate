package ru.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private List<Film> films = new ArrayList<>();
    private int counter;
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.add(film);
        return film;
    }

    private int generateId() {
        return ++counter;
    }


    public Film updateFilm(int id, Film updatedFilm) {
        Film filmToUpdate = films.stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElse(null);

        if (filmToUpdate != null) {
            filmToUpdate = filmToUpdate.toBuilder()
                    .name(updatedFilm.getName())
                    .description(updatedFilm.getDescription())
                    .releaseDate(updatedFilm.getReleaseDate())
                    .duration(updatedFilm.getDuration())
                    .build();
        }

        return filmToUpdate;
    }

    public List<Film> getAllFilms() {
        return films;
    }
}