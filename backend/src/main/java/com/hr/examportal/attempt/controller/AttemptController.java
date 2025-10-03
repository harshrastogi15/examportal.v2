package com.hr.examportal.attempt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attempt")
public class AttemptController {

    @PostMapping
    public ResponseEntity<Void> getAllQuestionId() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
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
