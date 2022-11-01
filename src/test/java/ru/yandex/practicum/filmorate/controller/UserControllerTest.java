package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();;

    @Test
    public void incorrectEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \" \",\n  \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"email\",\n  \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectBirthday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"2122-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}