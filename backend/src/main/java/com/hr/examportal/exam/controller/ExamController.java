package com.hr.examportal.exam.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.dto.ReadExamDto;
import com.hr.examportal.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam")
public class ExamController {
    private final ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ReadExamDto> createExam(@Valid @RequestBody CreateExamDto dto){
        Integer totalSum = dto.getEasyLevelMark() * dto.getNoQuestionPerLevel().get(0) + dto.getMediumLevelMark() * dto.getNoQuestionPerLevel().get(1) + dto.getHardLevelMark()*dto.getNoQuestionPerLevel().get(2) + dto.getSubLevelMark()* dto.getNoQuestionPerLevel().get(3);
        if(!dto.getTotalMarks().equals(totalSum)){
            throw new IllegalArgumentException("Invalid Marks");
        }
        return ResponseEntity.ok(examService.createExam(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ReadExamDto> getExam(@RequestBody JsonNode payload){
        UUID examId = UUID.fromString(payload.get("examId").asText());
        return ResponseEntity.ok(examService.getExamDetails(examId));
    }

    @PutMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ReadExamDto> updateExam(@RequestBody JsonNode payload){
        UUID examId = UUID.fromString(payload.get("examId").asText());
        JsonNode data = payload.get("data");
        CreateExamDto dto;
        try {
            dto = objectMapper.readerFor(CreateExamDto.class)
                    .with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(data);
        }catch (IOException e) {
            throw new IllegalArgumentException("Invalid Data");
        }
        Integer totalSum = dto.getEasyLevelMark() * dto.getNoQuestionPerLevel().get(0) + dto.getMediumLevelMark() * dto.getNoQuestionPerLevel().get(1) + dto.getHardLevelMark()*dto.getNoQuestionPerLevel().get(2) + dto.getSubLevelMark()* dto.getNoQuestionPerLevel().get(3);
        if(!dto.getTotalMarks().equals(totalSum)){
            throw new IllegalArgumentException("Invalid Marks");
        }
        return ResponseEntity.ok(examService.updateExam(examId, dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteExam(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<Map<String,Object>>> getAllExam(){
        return ResponseEntity.ok(examService.getAllExam());
    }

    @PostMapping("ready")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> examReady(@RequestBody JsonNode payload){
        UUID examId = UUID.fromString(payload.get("examId").asText());
        return ResponseEntity.ok(examService.examReady(examId));
    }
}
