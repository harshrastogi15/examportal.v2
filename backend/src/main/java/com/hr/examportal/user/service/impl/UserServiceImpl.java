package com.hr.examportal.user.service.impl;

import com.hr.examportal.filter.entity.CustomUserPrincipal;
import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;
import com.hr.examportal.user.entity.User;
import com.hr.examportal.user.repository.UserRepository;
import com.hr.examportal.user.service.UserService;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
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
    public List<ReadUserDto> getAllStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        return userRepository.findAllByRoleName("Student").stream()
                .map(u -> {
                    ReadUserDto user = mapper.map(u, ReadUserDto.class);
                    UUID encodedId = idEncoder.encodeId(u.getId(),customUserPrincipal.getId());
                    user.setStudentId(encodedId);
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ReadUserDto getUserDetail() {
        UUID id = userId.getId();
        User user = userRepository.findById(id).orElseThrow();
        return mapper.map(user, ReadUserDto.class);
    }

    @Override
    public ReadUserDto getStudentDetail(UUID studentId) {
        UUID realId = idEncoder.decodeId(studentId,userId.getId());
        User user = userRepository.findById(realId).orElseThrow();
        ReadUserDto readUserDto = mapper.map(user,ReadUserDto.class);
        readUserDto.setStudentId(idEncoder.encodeId(realId,userId.getId()));
        return readUserDto;
    }

}

