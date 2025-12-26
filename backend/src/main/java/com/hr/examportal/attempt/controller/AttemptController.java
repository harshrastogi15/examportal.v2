package com.hr.examportal.attempt.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.attempt.service.AttemptService;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.question.dto.ReadQuestionStudent;
import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.AnswerSelection;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attempt")
public class AttemptController {
    private final AttemptService attemptService;
    @PostMapping
    public ResponseEntity<Map<String,Object>> getAllQuestionId(@RequestParam UUID assignmentId) {
        if(assignmentId == null){
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(attemptService.getAllQuestionId(assignmentId));
    }

    @PostMapping("/answer")
    public ResponseEntity<Map<String,Object>> storeAnswer(@RequestParam UUID assignmentId, @RequestParam UUID userQuestionId, @RequestBody JsonNode json){

        if(!json.hasNonNull("type") || !json.hasNonNull("selection")){
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }
        QuestionType questionType;
        AnswerSelection answerSelection;
        try {
            questionType = QuestionType.valueOf(json.get("type").asText());
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }
        try {
            answerSelection = AnswerSelection.valueOf(json.get("selection").asText());
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }

        if(answerSelection.equals(AnswerSelection.Answered)){
            if (questionType == QuestionType.MCQ) {
                if (!json.hasNonNull("option")) {
                    throw new IllegalArgumentException("Field 'option' is required for MCQ");
                }
                try {
                    AnswerOption.valueOf(json.get("option").asText());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Invalid option value");
                }
    //            if (json.hasNonNull("text")) {
    //                throw new IllegalArgumentException("Field 'text' is not allowed for MCQ");
    //            }
            }else if (questionType == QuestionType.Subjective) {
                if (!json.hasNonNull("text") || json.get("text").asText().isBlank()) {
                    throw new IllegalArgumentException("Field 'text' is required for SUBJECTIVE");
                }
    //            if (json.hasNonNull("option")) {
    //                throw new IllegalArgumentException("Field 'option' is not allowed for SUBJECTIVE");
    //            }
            }
        }

        return ResponseEntity.ok(attemptService.storeAnswer(assignmentId,userQuestionId,json));
    }
    @GetMapping("/question")
    public ResponseEntity<ReadQuestionStudent> getUserQuestion(@RequestParam UUID assignmentId, @RequestParam UUID userQuestionId){
        return ResponseEntity.ok(attemptService.getQuestion(assignmentId,userQuestionId));
    }



}
