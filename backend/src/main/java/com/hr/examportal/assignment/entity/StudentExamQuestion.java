package com.hr.examportal.assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "student_exam_questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StudentExamQuestion {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "exam_assigned_id", nullable = false)
    private UUID examAssignedId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "exam_id", nullable = false)
    private UUID examId;

    @Column(name = "evaluation")
    private Integer evaluation;

    @Column(name = "assign_at", updatable = false)
    @CreatedDate
    private LocalDateTime assignAt;

}
