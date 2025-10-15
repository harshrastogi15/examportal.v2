package com.hr.examportal.attempt.controller;

import com.hr.examportal.attempt.service.AttemptService;
import com.hr.examportal.exception.CustomException;
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
    public ResponseEntity<Void> storeAnswer(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    @GetMapping("/question")
    public ResponseEntity<Void> getUserQuestion(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }



}
