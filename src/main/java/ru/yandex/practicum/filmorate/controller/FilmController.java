package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private Validator validator;

    @Autowired
    public FilmController(FilmService filmService, Validator validator) {
        this.filmService = filmService;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        Film createdFilm = filmService.addFilm(film);
        log.info("Film added: {}", createdFilm);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        Film updatedFilm = filmService.updateFilm(id, film);
        log.info("Film updated: {}", updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        log.info("Retrieved {} films", films.size());
        return ResponseEntity.ok(films);
    }
}