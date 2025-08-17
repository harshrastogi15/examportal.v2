package com.hr.examportal.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.user.dto.CreateUserDto;
import com.hr.examportal.user.dto.ReadUserDto;
import com.hr.examportal.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<ReadUserDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/students")
    public ResponseEntity<List<ReadUserDto>> getAllStudent() {
        return ResponseEntity.ok(userService.getAllStudent());
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/student/detail")
    public ResponseEntity<ReadUserDto> getStudentDetail(@RequestBody JsonNode payload){
        UUID studentId = UUID.fromString(payload.get("studentId").asText());
        return ResponseEntity.ok(userService.getStudentDetail(studentId));
    }

    @GetMapping("/detail")
    public ResponseEntity<ReadUserDto> getUserDetail() {
        return ResponseEntity.ok(userService.getUserDetail());
    }

}
