package com.hr.examportal.question.entity;

import com.hr.examportal.utils.enums.AnswerOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "question_option")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class QuestionOption {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "option_label", nullable = false, columnDefinition = "option_enum")
    private AnswerOption optionLabel;

    @Column(name = "option")
    private String option;

    @Column(name = "image_url")
    private String imageUrl;
}
