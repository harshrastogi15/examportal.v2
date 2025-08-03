package com.hr.examportal.user.service;

import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ReadUserDto createUser(CreateUserDto userDto);
    List<CreateUserDto> getAllUsers();
    CreateUserDto getUserById(UUID id);
    CreateUserDto updateUser(UUID id, CreateUserDto userDto);
}

