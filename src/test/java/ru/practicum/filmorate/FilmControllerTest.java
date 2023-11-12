package ru.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.util.Arrays;
import java.util.List;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Test
    public void testGetAllFilms() throws Exception {
        List<Film> films = Arrays.asList(new Film(), new Film());

        Mockito.when(filmService.getAllFilms()).thenReturn(films);

        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPopularFilms() throws Exception {
        List<Long> popularFilms = Arrays.asList(1L, 2L, 3L);

        Mockito.when(filmService.getPopularFilms()).thenReturn(popularFilms);

        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUnlikeFilm() throws Exception {
        Mockito.when(filmService.getFilmById(Mockito.anyInt())).thenReturn(new Film());

        mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLikeFilm() throws Exception {
        Mockito.when(filmService.getFilmById(Mockito.anyInt())).thenReturn(new Film());

        mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}