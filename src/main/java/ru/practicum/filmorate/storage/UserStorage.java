package ru.practicum.filmorate.storage;

import ru.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long userId);

    void validate(User user);

    void validateId(User user);

    void validateName(User user);
}
