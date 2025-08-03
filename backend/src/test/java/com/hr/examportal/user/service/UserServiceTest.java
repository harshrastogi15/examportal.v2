package com.hr.examportal.user.service;

import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;
import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import com.hr.examportal.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserRepository repo = mock(UserRepository.class);
    private final ModelMapper mapper = new ModelMapper();
    private final UserService service = new UserServiceImpl(repo, mapper);

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        CreateUserDto dto = new CreateUserDto("John", "john@test.com");
        when(repo.existsByEmail("john@test.com")).thenReturn(false);

        User user = mapper.map(dto, User.class);
        user.setId(UUID.randomUUID());
        when(repo.save(any(User.class))).thenReturn(user);

        ReadUserDto result = service.createUser(dto);
        assertEquals("John", result.getName());
    }
}
