package com.hr.examportal.user.service.impl;

import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;
import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import com.hr.examportal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public ReadUserDto createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        User user = mapper.map(dto, User.class);
        user.setRole("Student");
        return mapper.map(userRepository.save(user), ReadUserDto.class);
    }

    @Override
    public List<CreateUserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> mapper.map(u, CreateUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CreateUserDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return mapper.map(user, CreateUserDto.class);
    }

    @Override
    public CreateUserDto updateUser(UUID id, CreateUserDto dto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(dto.getName());
        return mapper.map(userRepository.save(user), CreateUserDto.class);
    }
}

