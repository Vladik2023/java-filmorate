package ru.practicum.filmorate.storage;

import ru.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void validate(Film film);

    void validateId(Film film);

    Film getFilmById(int id);
}
