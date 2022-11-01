package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();;

    @Test
    public void incorrectName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType("application/json")
                        .content("{\n  \"name\": \"\",\n  \"description\": " +
                                "\"Description\",\n  \"releaseDate\": \"1900-03-25\",\n  \"duration\": 200\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType("application/json")
                        .content("{\n  \"name\": \"\",\n  \"description\"" +
                                ": \"DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription\"" +
                                ",\n  \"releaseDate\": \"1900-03-25\",\n  \"duration\": 200\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectReleaseDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType("application/json")
                        .content("{\n  \"name\": \"name\",\n  \"description\": " +
                                "\"Description\",\n  \"releaseDate\": \"1700-03-25\",\n  \"duration\": 200\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectDuration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType("application/json")
                        .content("{\n  \"name\": \"name\",\n  \"description\": " +
                                "\"Description\",\n  \"releaseDate\": \"1700-03-25\",\n  \"duration\": -100\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}