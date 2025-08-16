package com.hr.examportal.exam.controller;

import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam")
public class ExamController {
    private final ExamService examService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> createExam(@Valid @RequestBody CreateExamDto dto){
        Integer totalSum = 0;
        if(!dto.getTotalMarks().equals(totalSum)){
            throw new IllegalArgumentException("Invalid Marks");
        }
        examService.createExam(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> getExam(){
        return ResponseEntity.ok().build();
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
