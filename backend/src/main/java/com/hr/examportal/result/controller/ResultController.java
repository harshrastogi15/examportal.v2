package com.hr.examportal.result.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
public class ResultController {

    private final ResultService resultService;

    @GetMapping
    public ResponseEntity<Void> getResult(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/calculate/mcq")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String,Object>> generateResultMCQ(@RequestBody JsonNode json){
        if (json == null || !json.has("assignmentIds") || !json.get("assignmentIds").isArray()) {
            throw new CustomException("Invalid Data",HttpStatus.BAD_REQUEST);
        }

        List<UUID> assignmentIds = new ArrayList<>();
        for (JsonNode node : json.get("assignmentIds")) {
            try {
                assignmentIds.add(UUID.fromString(node.asText()));
            } catch (IllegalArgumentException ex) {
                throw new CustomException("Invalid Data",HttpStatus.BAD_REQUEST);
            }
        }

        return ResponseEntity.ok(resultService.calculateResultMCQ(json));
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
