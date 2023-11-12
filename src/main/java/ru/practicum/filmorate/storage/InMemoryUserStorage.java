package ru.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.utils.Validators;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();


    public User createUser(User user) {
        validate(user);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Ошибка! Невозможно обновить пользователя - его не существует.");
        }
        validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(long userId) {
        return users.get(userId);
    }

    public void validate(User user) {
        Validators.validateEmail(user.getEmail());
        Validators.validateLogin(user.getLogin());
        validateName(user);
        Validators.validateBirthday(user.getBirthday());
        validateId(user);
    }

    public void validateId(User user) {
        if (user.getId() == 0) {
            user.setId(User.usersId++);
        }
    }

    public void validateName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
