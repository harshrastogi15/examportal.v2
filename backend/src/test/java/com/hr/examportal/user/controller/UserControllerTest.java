package com.hr.examportal.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;
import com.hr.examportal.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Alice")
                .email("alice@example.com")
                .build();

        ReadUserDto savedDto = ReadUserDto.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .email("alice@example.com")
                .role("Student")
                .build();

        Mockito.when(userService.createUser(Mockito.any(CreateUserDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        UUID id = UUID.randomUUID();
        CreateUserDto dto = new CreateUserDto("Bob", "bob@example.com");

        Mockito.when(userService.getUserById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }
}

