package ru.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.utils.Validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) {
        validate(user);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            Validators.logAndError("Ошибка! Невозможно обновить пользователя - его не существует.");
        }
        validate(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) {
        Validators.validateEmail(user.getEmail());
        Validators.validateLogin(user.getLogin());
        validateName(user);
        Validators.validateBirthday(user.getBirthday());
        validateId(user);
    }

    private void validateId(User user) {
        if (user.getId() == 0) {
            user.setId(User.usersId++);
        }
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}