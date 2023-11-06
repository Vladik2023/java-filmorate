package ru.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.filmorate.controller.UserController;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.model.User;
import ru.practicum.filmorate.storage.UserStorage;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserStorage userStorage;

    @MockBean
    private Validator validator;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User(1, "user@email.com", "user", "Use", LocalDate.now());
        when(userStorage.createUser(any(User.class))).thenReturn(user);
        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User(1, "user@email.com", "user", "Use", LocalDate.now());
        when(userStorage.updateUser(any(User.class))).thenReturn(user);
        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User(1, "user@email.com", "user", "Use", LocalDate.now());
        when(userStorage.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(user.getName()));
    }
}