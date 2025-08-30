package com.hr.examportal.question.entity;


import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Question {
    @Id
    private UUID id;

    @Column(name = "exam_id", nullable = false)
    private UUID examId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "question_type", columnDefinition = "question_type_enum", nullable = false)
    private QuestionType questionType;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_image_url")
    private String questionImageUrl;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "correct_answer", columnDefinition = "option_enum")
    private AnswerOption correctAnswer;

    @Column(name = "suggested_answer", columnDefinition = "text[]")
    private List<String> suggestedAnswer;


    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "difficulty", columnDefinition = "difficulty_level_enum")
    private DifficultyLevel difficulty;

    @Column(name = "assigned")
    private Boolean assigned;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
