package ru.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.filmorate.model.Genre;
import ru.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage  {

    private final JdbcTemplate jdbcTemplate;

   @Override
    public Map<Integer, Genre> getAllGenres() {
        Map<Integer, Genre> allGenre = new HashMap<>();
        String sqlQuery = "SELECT * FROM GENRE;";
        List<Genre> genreFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        for (Genre genre : genreFromDb) {
            allGenre.put(genre.getId(), genre);
        }
        return allGenre;
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("ID"),
                    genreRows.getString("NAME"));
            log.info("Найден жанр с id {}", id);
            return Optional.of(genre);
        }
        log.warn("Жанр с id {} не найден", id);
        return Optional.empty();
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("ID"), rs.getString("NAME"));
    }
}