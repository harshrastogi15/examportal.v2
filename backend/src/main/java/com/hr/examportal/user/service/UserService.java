package com.hr.examportal.user.service;

import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ReadUserDto createUser(CreateUserDto userDto);
    List<ReadUserDto> getAllStudent();
    ReadUserDto getUserDetail();

    ReadUserDto getStudentDetail(UUID studentId);
}

