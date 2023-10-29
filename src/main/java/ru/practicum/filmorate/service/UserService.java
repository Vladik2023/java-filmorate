package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();

    public User createUser(User user) {
        users.add(user);
        return user;
    }

    public User updateUser(int id, User updatedUser) {
        User userToUpdate = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);

        if (userToUpdate != null) {
            userToUpdate = userToUpdate.toBuilder()
                    .email(updatedUser.getEmail())
                    .login(updatedUser.getLogin())
                    .birthday(updatedUser.getBirthday())
                    .build();
        }

        return userToUpdate;
    }

    public List<User> getAllUsers() {
        return users;
    }
}