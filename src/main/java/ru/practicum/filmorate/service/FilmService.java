package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.storage.FilmStorage;

import javax.validation.ValidationException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некоректная дата релиза");
        }

    }

    public Film getById(Long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.getFilmById(filmId).getLikesUser().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.getFilmById(filmId).getLikesUser().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikesUser().size() - o1.getLikesUser().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}