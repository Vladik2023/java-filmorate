package ru.practicum.filmorate.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.exception.ValidationException;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        Set<ConstraintViolation<Film>> violations = filmService.validateFilm(film);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            throw new ValidationException(errorMessage);
        }

        Film createdFilm = filmService.addFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping
    @Validated
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Set<ConstraintViolation<Film>> violations = filmService.validateFilm(film);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            throw new ValidationException(errorMessage);
        }

        Film updatedFilm = filmService.updateFilm(film);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Long>> getPopularFilms() {
        List<Long> popularFilms = filmService.getPopularFilms();
        return ResponseEntity.ok(popularFilms);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> unlikeFilm(@PathVariable int id, @PathVariable Long userId) {
        Film film = filmService.getFilmById(id);
        if (film == null) {
            return ResponseEntity.notFound().build();
        }

        filmService.removeLike((long) film.getId(), userId);
        return ResponseEntity.ok("Лайк с фильма удален успешно");
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable int id, @PathVariable long userId) {
        Film film = filmService.getFilmById(id);
        if (film == null) {
            return ResponseEntity.notFound().build();
        }

        filmService.addLike((long) film.getId(), userId);
        return ResponseEntity.ok("Фильм лайкнут успешно");
    }
}