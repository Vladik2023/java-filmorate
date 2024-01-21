package ru.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.model.Genre;
import ru.practicum.filmorate.model.Mpa;
import ru.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT F.*, R.ID MPA_ID, R.NAME MPA_NAME FROM FILMS AS F JOIN RATING AS R ON F.RATING_ID = R.ID ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }


    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        String queryForFilmGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, RATING_ID = ?, DURATION = ?" +
                " WHERE ID = ?;";
        String queryToDeleteFilmGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?;";
        String queryForUpdateGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getMpa().getId(), film.getDuration(), film.getId());
        jdbcTemplate.update(queryToDeleteFilmGenres, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForUpdateGenre, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlQuery = "SELECT F.*, R.ID MPA_ID, R.NAME MPA_NAME FROM FILMS AS F JOIN RATING AS R ON F.RATING_ID = R.ID " +
                " WHERE F.ID = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("ID"))
                    .name(filmRows.getString("NAME"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate())
                    .duration(filmRows.getInt("DURATION"))
                    .mpa(new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("MPA_NAME")))
                    .build();

            film.setGenres(getGenresOfFilm(id).stream().sorted(Comparator.comparingInt(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            film.setLikesUser(new HashSet<>(getLikesOfFilm(film.getId())));

            log.info("Найден фильм с id {}", id);
            return film;
        }
        log.warn("Фильм с id {} не найден", id);
        throw new NotFoundException("Что то не работает");
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Film build start>>>>>");
        Film film = Film.builder()
                .id(rs.getLong("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                .build();
        log.info("Film = {}", film);

        film.setGenres(getGenresOfFilm(film.getId()).stream().sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        film.setLikesUser(new HashSet<>(getLikesOfFilm(film.getId())));

        return film;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }

    private Long mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("ID");
    }

    private List<Genre> getGenresOfFilm(long filmId) {
        String queryForFilmGenres = "SELECT FG.FILM_ID, FG.GENRE_ID, G.NAME FROM FILM_GENRE FG" +
                " JOIN GENRE G ON G.ID = FG.GENRE_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::mapRowToGenre, filmId);
    }

    @Override
    public List<Long> getLikesOfFilm(long filmId) {
        String queryForFilmLikes = "SELECT USER_ID ID FROM FILM_LIKES WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmLikes, this::mapRowToLike, filmId);
    }

    @Override
    public Map<Long, Set<Long>> getLikesOfFilm(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        String queryForFilmLikes = "SELECT FILM_ID, USER_ID FROM FILM_LIKES WHERE FILM_ID IN (?);".replace("?", inSql);

        Map<Long, Set<Long>> result = films.stream().collect(Collectors.toMap(Film::getId, Film::getLikesUser));

        jdbcTemplate.query(queryForFilmLikes, (ResultSet rs) -> {
            long filmId = rs.getLong("FILM_ID");
            long userId = rs.getLong("USER_ID");
            result.get(filmId).add(userId);
        }, result.keySet().toArray());

        return result;
    }


    @Override
    public void addLike(long filmId, long userId) {
        try {
            String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?);";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (Exception e) {
            log.warn("Лайк фильму с id {} от пользователя с id {} уже существует", filmId, userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }


    public void deleteFilm(long filmId) {
        getFilmById(filmId);
        String sqlQuery = "DELETE FROM FILMS WHERE ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private List<Film> getFilmsByTitle(String query) {
        String sql = "SELECT f.*, r.NAME FROM FILMS f INNER JOIN RATING r ON r.ID = f.RATING_ID " +
                "LEFT JOIN FILM_LIKES fl ON fl.FILM_ID = f.ID GROUP BY f.FILM_ID " +
                "HAVING LOWER(f.NAME) LIKE ? ORDER BY COUNT(fl.USER_ID) DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, query);
    }

    @Override
    public List<Film> getRecommendations(long userId) {
        /*
        Описание запроса:
        Подзапрос (1) - Найти id пользователей, с максимальным количеством пересечения по лайкам:
        Объединям таблицу FILM_LIKES с самой собой, делаем right join
        и оставляем справа только id данного пользователя, слева id всех пользователей, кроме нашего и null.
        Получается такая таблица: FL1.user_id | film_id | FL2.user_id
        Таким образом в FL1.user_id получаются id всех пользователей, которые лайкали такие же фильмы, что и данный.
        Группируем пользователей по id, сортируем по частоте этих id
        (то есть в начале списка будут пользователи, у которых наиболее совпадают лайки с данным).
        Выбираем первых трех из этих пользователей

        Подзапрос (2):
        Находим id фильмов, которые лайкнули найденные пользователи

        Подзапрос (3):
        Находим id фильмов, которые лайкнул данный пользователь

        Запрос (4):
        Находим фильмы, c id, которые есть в списке из (2), но нет в списке из (3).
        (То есть те, которые лайкали найденные пользователи, на не лайкал данный)
         */
        String sql =
                "SELECT * FROM FILMS F " + //(4)
                        "JOIN RATING R ON F.RATING_ID = R.ID " +
                        "WHERE F.ID IN (" +
                        "SELECT FILM_ID FROM FILM_LIKES " + //(2)
                        "WHERE USER_ID IN (" +
                        "SELECT FL1.USER_ID FROM FILM_LIKES FL1 " + //(1)
                        "RIGHT JOIN FILM_LIKES FL2 ON FL2.FILM_ID = FL1.FILM_ID " +
                        "GROUP BY FL1.USER_ID, FL2.USER_ID " +
                        "HAVING FL1.USER_ID IS NOT NULL AND " +
                        "FL1.USER_ID != ? AND " +
                        "FL2.USER_ID = ? " +
                        "ORDER BY COUNT(FL1.USER_ID) DESC " +
                        "LIMIT 3 " +
                        ") " +
                        "AND FILM_ID NOT IN (" +
                        "SELECT FILM_ID FROM FILM_LIKES " + //(3)
                        "WHERE USER_ID = ?" +
                        ")" +
                        ")";

        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, userId, userId);
    }
}
