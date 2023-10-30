package ru.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(int id, User updatedUser) {
        users.put(id, updatedUser);
        return updatedUser;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}