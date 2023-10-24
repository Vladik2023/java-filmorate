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
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Validator;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTest {

    @Mock
    private FilmService filmService;


    @Mock
    private Validator validator;

    @InjectMocks
    private FilmController filmController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    public void testAddFilmWhenValidFilmThenReturnCreated() throws Exception {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");

        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());
        when(filmService.addFilm(any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Film\",\"description\":\"Test Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"Test Film\",\"description\":\"Test Description\"}"));
    }

    @Test
    public void testUpdateFilmWhenValidFilmThenReturnOk() throws Exception {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");

        when(validator.validate(any(Film.class))).thenReturn(Collections.emptySet());
        when(filmService.updateFilm(any(Integer.class), any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Film\",\"description\":\"Test Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Test Film\",\"description\":\"Test Description\"}"));
    }

    @Test
    public void testGetAllFilmsWhenFilmsExistThenReturnOk() throws Exception {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");

        List<Film> films = List.of(film1, film2);

        when(filmService.getAllFilms()).thenReturn(films);

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Film 1\",\"description\":\"Description 1\"},{\"name\":\"Film 2\",\"description\":\"Description 2\"}]"));
    }
}