package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    private static final LocalDate presentDate = LocalDate.now();
    private static final String stringPresentDate = String.valueOf(presentDate);
    @Autowired
    MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();;

    @Test
    public void incorrectEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \" \",\n "
                                + " \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"email\",\n "
                                + " \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void incorrectLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n "
                                + " \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login login\",\n  \"name\": \"name\",\n  \"email\": "
                                + " \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void voidName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"\",\n  \"email\": \"mail@mail.ru\",\n "
                                + " \"birthday\": \"1946-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n \"id\": 1,\n \"login\": \"login\",\n "
                        + " \"name\": \"login\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}"));
    }

    @Test
    public void incorrectBirthday() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n "
                                + " \"birthday\": \"2122-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n "
                                + " \"birthday\": \"" + stringPresentDate + "\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n "
                                + "  \"birthday\": \"1999-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateAddedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content("{\n  \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": \"mail@mail.ru\",\n "
                                + "  \"birthday\": \"1999-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType("application/json")
                        .content("{\n \"id\": 1,\n \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": "
                                + "\"mail@mail.ru\",\n  \"birthday\": \"1999-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateNotAddedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType("application/json")
                        .content("{\n \"id\": 3,\n \"login\": \"login\",\n  \"name\": \"name\",\n  \"email\": "
                                + "\"mail@mail.ru\",\n  \"birthday\": \"1999-08-20\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}