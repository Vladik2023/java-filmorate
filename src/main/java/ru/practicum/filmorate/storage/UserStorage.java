package ru.practicum.filmorate.storage;

import ru.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long userId);

    void addFriend(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<User> getAllFriends(long userId);

    void deleteUser(long userId);
}
