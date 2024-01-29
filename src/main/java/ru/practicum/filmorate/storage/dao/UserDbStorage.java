package ru.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM \"USERS\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO \"USERS\" (EMAIL, LOGIN, BIRTHDAY, NAME) VALUES (?, ?, ?, ?)";

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sqlQuery, new String[]{"id"});
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getLogin());
                    ps.setDate(3, Date.valueOf(user.getBirthday()));
                    ps.setString(4, user.getName());
                    return ps;
                },
                keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE \"USERS\" SET EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? WHERE ID = ?";

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(long id) {
        String sqlQuery = "SELECT * FROM \"USERS\" WHERE ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            User user = User.builder()
                    .email(userRows.getString("EMAIL"))
                    .login(userRows.getString("LOGIN"))
                    .name(userRows.getString("NAME"))
                    .id(userRows.getLong("ID"))
                    .birthday((Objects.requireNonNull(userRows.getDate("BIRTHDAY"))).toLocalDate())
                    .build();
            log.info("Найден пользователь с id {}", id);
            return user;
        }
        log.warn("Пользователь с id {} не найден", id);
        throw new NotFoundException("что то не работает");
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .id(rs.getLong("ID"))
                .birthday((rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "INSERT INTO USER_FRIENDS (USER_ID, FRIENDS_ID) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sqlQuery = "DELETE FROM USER_FRIENDS WHERE USER_ID = ? AND FRIENDS_ID = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getMutualFriends(long userId, long otherUserId) {
        String sqlQuery = "SELECT * FROM \"USERS\" AS U WHERE U.ID IN (SELECT F.FRIENDS_ID " +
                "FROM USER_FRIENDS AS F WHERE F.USER_ID = ? " +
                "INTERSECT SELECT F.FRIENDS_ID FROM USER_FRIENDS AS F WHERE F.USER_ID = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        getUserById(userId);
        String sqlQuery = "SELECT * FROM \"USERS\" AS U WHERE U.ID IN " +
                "(SELECT F.FRIENDS_ID FROM USER_FRIENDS AS F WHERE F.USER_ID = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public void deleteUser(long userId) {
        getUserById(userId);
        String sqlQuery = "DELETE FROM \"USERS\" WHERE ID = ?;";
        jdbcTemplate.update(sqlQuery, userId);
    }
}
