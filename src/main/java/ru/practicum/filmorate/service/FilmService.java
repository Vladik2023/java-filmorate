package ru.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FilmService {
    private Map<Long, Set<Long>> likesMap;
    private Map<Long, Integer> likesCountMap;

    public FilmService() {
        likesMap = new HashMap<>();
        likesCountMap = new HashMap<>();
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