package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private List<Film> films = new ArrayList<>();

    public Film addFilm(Film film) {
        films.add(film);
        return film;
    }

    public Film updateFilm(int id, Film updatedFilm) {
        Film filmToUpdate = films.stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElse(null);

        if (filmToUpdate != null) {
            filmToUpdate= filmToUpdate.toBuilder()
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