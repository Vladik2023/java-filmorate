//package ru.practicum.filmorate;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.practicum.filmorate.controller.UserController;
//import ru.practicum.filmorate.model.User;
//import ru.practicum.filmorate.service.UserService;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Test
//    void createUser_ValidUser_ReturnsCreatedUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setLogin("testuser");
//
//        User createdUser = new User();
//        createdUser.setId(1L);
//        createdUser.setEmail("test@example.com");
//        createdUser.setLogin("testuser");
//
//        when(userService.createUser(user)).thenReturn(createdUser);
//
//        User result = userController.createUser(user);
//
//        assertEquals(createdUser, result);
//        verify(userService, times(1)).createUser(user);
//    }
//
//    @Test
//    void updateUser_ValidUser_ReturnsUpdatedUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        user.setLogin("testuser");
//
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setEmail("updated@example.com");
//        updatedUser.setLogin("testuser");
//
//        when(userService.updateUser(user)).thenReturn(updatedUser);
//        User result = userController.updateUser(user);
//        assertEquals(updatedUser, result);
//        verify(userService, times(1)).updateUser(user);
//    }
//
//    @Test
//    void getAllUsers_ReturnsListOfUsers() {
//        List<User> users = Collections.singletonList(new User());
//
//        when(userService.getAllUsers()).thenReturn(users);
//
//        List<User> result = userController.getAllUsers();
//
//        assertEquals(users, result);
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void getUserById_ExistingId_ReturnsUser() {
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        when(userService.getUserById(userId)).thenReturn(user);
//
//        User result = userController.getUserById(userId);
//
//        assertEquals(user, result);
//        verify(userService, times(1)).getUserById(userId);
//    }
//
//    @Test
//    void addFriend_ValidIds_NoContentReturned() {
//        Long userId = 1L;
//        Long friendId = 2L;
//
//        assertDoesNotThrow(() -> userController.addFriend(userId, friendId));
//
//        verify(userService, times(1)).addFriend(userId, friendId);
//    }
//
//    @Test
//    void removeFriend_ValidIds_NoContentReturned() {
//        Long userId = 1L;
//        Long friendId = 2L;
//
//        assertDoesNotThrow(() -> userController.removeFriend(userId, friendId));
//
//        verify(userService, times(1)).removeFriend(userId, friendId);
//    }
//
//    @Test
//    void getFriends_ValidId_ReturnsListOfFriends() {
//        Long userId = 1L;
//        List<User> friends = Collections.singletonList(new User());
//
//        when(userService.getFriends(userId)).thenReturn(friends);
//
//        List<User> result = userController.getFriends(userId);
//
//        assertEquals(friends, result);
//        verify(userService, times(1)).getFriends(userId);
//    }
//
//    @Test
//    void getCommonFriends_ValidIds_ReturnsListOfCommonFriends() {
//        Long userId = 1L;
//        Long otherId = 2L;
//        List<User> commonFriends = Collections.singletonList(new User());
//
//        when(userService.getCommonFriends(userId, otherId)).thenReturn(commonFriends);
//
//        List<User> result = userController.getCommonFriends(userId, otherId);
//
//        assertEquals(commonFriends, result);
//        verify(userService, times(1)).getCommonFriends(userId, otherId);
//    }
//}