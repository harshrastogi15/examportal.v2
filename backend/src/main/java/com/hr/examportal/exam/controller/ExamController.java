package com.hr.examportal.exam.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.dto.ReadExamDto;
import com.hr.examportal.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam")
public class ExamController {
    private final ExamService examService;

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
        UUID studentId = UUID.fromString(payload.get("examId").asText());
        return ResponseEntity.ok(examService.getExamDetails(studentId));
    }

    @PutMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> updateExam(){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteExam(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> getAllExam(){
        return ResponseEntity.ok().build();
    }

}
