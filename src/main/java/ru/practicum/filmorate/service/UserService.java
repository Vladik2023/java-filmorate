package ru.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();
    private int counter;

    public User createUser(User user) {
        user.setId(generateId());
        users.add(user);
        return user;
    }

    private int generateId(){
        return ++counter;
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