package com.hr.examportal.result.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
public class ResultController {

    @GetMapping
    public ResponseEntity<Void> getResult(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/calculate/mcq")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> generateResultMCQ(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    @PostMapping("/calculate/subjective")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> generateResultSubjective(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> generateResult(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }



}
