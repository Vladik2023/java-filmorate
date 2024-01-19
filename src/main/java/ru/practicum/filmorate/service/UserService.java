package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private Map<Long, Set<Long>> friendsMap;

    private UserStorage userStorage;

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
        userStorage.getUserById(id).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(id);
    }

    public void removeFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
    }

    public List<User> getFriends(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Не найден пользователь");
        }

        Set<Long> friends = userStorage.getUserById(id).getFriends();
        Set<Long> otherFriends = userStorage.getUserById(friendId).getFriends();

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public User getUserById(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    private boolean areFriends(Long userId1, Long userId2) {
        Set<Long> user1Friends = friendsMap.get(userId1);
        return user1Friends != null && user1Friends.contains(userId2);
    }
}