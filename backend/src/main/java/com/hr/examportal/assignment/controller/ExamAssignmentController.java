package com.hr.examportal.assignment.controller;

import com.hr.examportal.assignment.dto.ExamAssignDto;
import com.hr.examportal.assignment.service.ExamAssignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assign")
public class ExamAssignmentController {
    private final ExamAssignService examAssignService;

    @PostMapping("/exam")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String,String>> assignExam(@RequestBody @Valid ExamAssignDto dto){
        return ResponseEntity.ok(examAssignService.assignExam(dto));
    }


    @DeleteMapping("/exam")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String,String>> deleteAssignedExam(@RequestBody @Valid ExamAssignDto dto){
        return ResponseEntity.ok(examAssignService.deleteAssignedExam(dto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<Map<String,Object>>> getAllUsersAssigned(@RequestParam UUID examId){
        return ResponseEntity.ok(examAssignService.getAllUsersAssigned(examId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String,Object>>> getAllAssignment(){
        return ResponseEntity.ok(examAssignService.getAllAssignment());
    }

}
