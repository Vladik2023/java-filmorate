package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.storage.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private Map<Long, Set<Long>> likesMap;
    private Map<Long, Integer> likesCountMap;
    private FilmStorage filmStorage;
    private Validator validator;

    public Set<ConstraintViolation<Film>> validateFilm(Film film) {
        return validator.validate(film);
    }

    public Film addFilm(Film film){
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film){
        filmStorage.updateFilm(film);
        return film;
    }
    public List<Film> getAllFilms(){
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        filmStorage.getFilmById(id);
        return filmStorage.getFilmById(id);
    }
    public void addLike(Long filmId, Long userId) {
        if (!likesMap.containsKey(filmId)) {
            likesMap.put(filmId, new HashSet<>());
        }

        Set<Long> likes = likesMap.get(filmId);
        if (!likes.contains(userId)) {
            likes.add(userId);
            likesCountMap.put(filmId, likes.size());
        }
    }

    public void removeLike(Long filmId, Long userId) {
        if (likesMap.containsKey(filmId)) {
            Set<Long> likes = likesMap.get(filmId);
            if (likes.contains(userId)) {
                likes.remove(userId);
                likesCountMap.put(filmId, likes.size());
            }
        }
    }

    public List<Long> getPopularFilms() {
        List<Map.Entry<Long, Integer>> sortedLikesCountList = new ArrayList<>(likesCountMap.entrySet());
        sortedLikesCountList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<Long> popularFilms = new ArrayList<>();
        int count = 0;
        for (Map.Entry<Long, Integer> entry : sortedLikesCountList) {
            popularFilms.add(entry.getKey());
            count++;
            if (count >= 10) {
                break;
            }
        }

        return popularFilms;
    }
}