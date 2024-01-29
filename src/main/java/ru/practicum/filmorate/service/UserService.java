package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("Ошибка! Невозможно обновить пользователя - его не существует.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        userStorage.removeFromFriends(id, friendId);
    }

    public List<User> getFriends(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        return userStorage.getAllFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long friendId) {


        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь");
        }

        Set<User> friends = new HashSet<>(userStorage.getAllFriends(id));
        Set<User> otherFriends = new HashSet<>(userStorage.getAllFriends(friendId));

        if (friends.isEmpty() || otherFriends.isEmpty()) {
            return new ArrayList<>();
        }

        return friends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }

    public User getUserById(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }
}