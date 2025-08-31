package com.hr.examportal.question.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.question.dto.CreateQuestionDto;
import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ReadQuestionInstructor> createQuestion(@Valid @RequestBody CreateQuestionDto dto){

        return ResponseEntity.ok(questionService.createQuestion(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> getQuestion(@RequestBody JsonNode payload){
        UUID questionId = UUID.fromString(payload.get("questionId").asText());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PutMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void>  updateQuestion(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteQuestion(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> getAllQuestion(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
