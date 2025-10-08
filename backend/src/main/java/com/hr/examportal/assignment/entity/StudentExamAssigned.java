package com.hr.examportal.assignment.entity;

import com.hr.examportal.utils.enums.StatusStage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "student_exam_assigned")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StudentExamAssigned {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "exam_id", nullable = false)
    private UUID examId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "status_enum", nullable = false)
    private StatusStage status = StatusStage.Pending;

    private Integer score;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "assign_at", updatable = false)
    @CreatedDate
    private LocalDateTime assignAt;

}
