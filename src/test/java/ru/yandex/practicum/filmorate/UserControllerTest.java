package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUserWhenValidUserThenReturnCreated() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testLogin");

        when(validator.validate(any(User.class))).thenReturn(Collections.emptySet());
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"login\":\"testLogin\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"email\":\"test@test.com\",\"login\":\"testLogin\"}"));
    }

    @Test
    public void testUpdateUserWhenValidUserThenReturnOk() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testLogin");

        when(validator.validate(any(User.class))).thenReturn(Collections.emptySet());
        when(userService.updateUser(any(Integer.class), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"login\":\"testLogin\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@test.com\",\"login\":\"testLogin\"}"));
    }
}
