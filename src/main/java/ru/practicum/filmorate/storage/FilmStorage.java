package ru.practicum.filmorate.storage;

import ru.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Map<Long, Film> getAllFilms();

    Film getFilmById(Long id);

    List<Film> getRecommendations(long userId);

    void deleteLike(long filmId, long userId);

    void addLike(long filmId, long userId);
}
