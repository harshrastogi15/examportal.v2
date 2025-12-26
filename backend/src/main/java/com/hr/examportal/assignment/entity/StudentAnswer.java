package com.hr.examportal.assignment.entity;


import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.AnswerSelection;
import com.hr.examportal.utils.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_answer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StudentAnswer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "submission_id", nullable = false)
    private UUID submissionId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "selected_option", columnDefinition = "option_enum")
    private AnswerOption selectedOption;

    @Column(name = "subjective_answer")
    private String subjectiveAnswer;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "mode", columnDefinition = "mode_enum", nullable = false)
    private AnswerSelection mode;

    @LastModifiedDate
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
}
