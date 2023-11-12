package ru.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@ComponentScan
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        Set<ConstraintViolation<User>> violations = userService.validateUser(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        User createdUser = userService.createUser(user);
        log.info("User created: {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        Set<ConstraintViolation<User>> violations = userService.validateUser(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        User updatedUser = userService.updateUser(user);
        log.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        log.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.getUserById(id);
        User friend = userService.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("User or friend not found");
        }
        userService.addFriend(id, friendId);
        return ResponseEntity.ok("Friend added successfully");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.getUserById(id);
        User friend = userService.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("User or friend not found");
        }
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok("Friend removed successfully");
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        List<User> friends = userService.getFriends(id);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        User user = userService.getUserById(id);
        User otherUser = userService.getUserById(otherId);
        if (user == null || otherUser == null) {
            throw new NotFoundException("User not found");
        }
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        return ResponseEntity.ok(commonFriends);
    }
}