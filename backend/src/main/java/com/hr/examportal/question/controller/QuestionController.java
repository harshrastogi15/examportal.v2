package com.hr.examportal.question.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.question.dto.CreateQuestionDto;
import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.dto.UpdateQuestionDto;
import com.hr.examportal.question.service.QuestionService;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<ReadQuestionInstructor> getQuestionInstructor(@RequestParam UUID id){
        return ResponseEntity.ok(questionService.getQuestionDetailsInstructor(id));
    }

    @PutMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ReadQuestionInstructor>  updateQuestion(@Valid @RequestBody UpdateQuestionDto dto){
        if(dto.getQuestionType().equals(QuestionType.MCQ) && (dto.getOptions()==null || (dto.getOptions().size()!=4))){
            throw new CustomException("Invalid size of option");
        }
        if(dto.getQuestionType().equals(QuestionType.Subjective) && (!dto.getDifficulty().equals(DifficultyLevel.Subjective))){
            throw new CustomException("For Type Subjective, difficulty should be Subjective");
        }
        return ResponseEntity.ok(questionService.updateQuestion(dto));
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
