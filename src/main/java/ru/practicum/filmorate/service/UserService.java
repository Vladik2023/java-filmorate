package ru.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private Map<Long, Set<Long>> friendsMap;
    @Autowired
    private UserStorage userStorage;
    public Validator validator;

    public Set<ConstraintViolation<User>> validateUser(User user) {
        return validator.validate(user);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers(){
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId1, Long userId2) {
        if (areFriends(userId1, userId2)) {
            System.out.println("Пользователь " + userId1 + " уже является другом пользователя " + userId2);
            return;
        }

        friendsMap.computeIfAbsent(userId1, k -> new HashSet<>()).add(userId2);
        friendsMap.computeIfAbsent(userId2, k -> new HashSet<>()).add(userId1);
    }

    public void removeFriend(Long userId1, Long userId2) {
        Set<Long> user1Friends = friendsMap.get(userId1);
        Set<Long> user2Friends = friendsMap.get(userId2);

        if (user1Friends != null) {
            user1Friends.remove(userId2);
        }

        if (user2Friends != null) {
            user2Friends.remove(userId1);
        }
    }

    public List<User> getFriends(Long userId) {
        Set<Long> friendIds = friendsMap.getOrDefault(userId, new HashSet<>());
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendIds) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        List<User> friends1 = getFriends(userId1);
        List<User> friends2 = getFriends(userId2);
        List<User> commonFriends = new ArrayList<>();
        for (User friend1 : friends1) {
            if (friends2.contains(friend1)) {
                commonFriends.add(friend1);
            }
        }
        return commonFriends;
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    private boolean areFriends(Long userId1, Long userId2) {
        Set<Long> user1Friends = friendsMap.get(userId1);
        return user1Friends != null && user1Friends.contains(userId2);
    }
}