package ru.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.controller.UserController;
import ru.practicum.filmorate.exception.NotFoundException;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testCreateUser() {
        User user = new User();
        when(userService.validateUser(user)).thenReturn(Collections.emptySet());
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).createUser(user);
    }

    @Test
    void testCreateUserWithValidationErrors() {
        User user = new User();
        Set<ConstraintViolation<User>> violations = Collections.singleton(mock(ConstraintViolation.class));
        when(userService.validateUser(user)).thenReturn(violations);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
        verify(userService, never()).createUser(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        when(userService.validateUser(user)).thenReturn(Collections.emptySet());
        when(userService.updateUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).updateUser(user);
    }

    @Test
    void testUpdateUserWithValidationErrors() {
        User user = new User();
        Set<ConstraintViolation<User>> violations = Collections.singleton(mock(ConstraintViolation.class));
        when(userService.validateUser(user)).thenReturn(violations);

        assertThrows(ValidationException.class, () -> userController.updateUser(user));
        verify(userService, never()).updateUser(user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Collections.singletonList(new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById() {
        User user = new User();
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userController.getUserById(1L));
        verify(userService).getUserById(1L);
    }

    @Test
    void testAddFriend() {
        userController.addFriend(1L, 2L);

        verify(userService).addFriend(1L, 2L);
    }

    @Test
    void testRemoveFriend() {
        userController.removeFriend(1L, 2L);

        verify(userService).removeFriend(1L, 2L);
    }

    @Test
    void testGetFriends() {
        List<User> friends = Collections.singletonList(new User());
        when(userService.getFriends(1L)).thenReturn(friends);

        ResponseEntity<List<User>> response = userController.getFriends(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(friends, response.getBody());
        verify(userService).getFriends(1L);
    }

    @Test
    void testGetCommonFriends() {
        List<User> commonFriends = Collections.singletonList(new User());
        when(userService.getCommonFriends(1L, 2L)).thenReturn(commonFriends);

        ResponseEntity<List<User>> response = userController.getCommonFriends(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commonFriends, response.getBody());
        verify(userService).getCommonFriends(1L, 2L);
    }
}