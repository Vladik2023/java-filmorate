package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Validator validator;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        User createdUser = userService.createUser(user);
        log.info("User created: {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        User updatedUser = userService.updateUser(id, user);
        log.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        log.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }
}