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
import ru.practicum.filmorate.controller.FilmController;
import ru.practicum.filmorate.model.Film;
import ru.practicum.filmorate.service.FilmService;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @MockBean
    private Validator validator;

    @Test
    public void testAddFilm() throws Exception {
        Film film = new Film(1, "Test Film", "Test Description", LocalDate.now(), 120);
        when(filmService.addFilm(any(Film.class))).thenReturn(film);
        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(film.getName()));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        Film film = new Film(1, "Test Film", "Test Description", LocalDate.now(), 120);
        when(filmService.updateFilm(any(Film.class))).thenReturn(film);
        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(film.getName()));
    }

    @Test
    public void testGetAllFilms() throws Exception {
        Film film = new Film(1, "Test Film", "Test Description", LocalDate.now(), 120);
        when(filmService.getAllFilms()).thenReturn(Collections.singletonList(film));

        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(film.getName()));
    }
}