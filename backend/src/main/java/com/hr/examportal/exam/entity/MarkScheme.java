package com.hr.examportal.exam.entity;

import com.hr.examportal.utils.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mark_scheme", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"exam_id", "difficulty"})
})
public class MarkScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "difficulty", columnDefinition = "difficulty_level_enum")
    private DifficultyLevel difficulty;

    @Column(nullable = false)
    private int mark;

    @JoinColumn(name = "exam_id", nullable = false)
    private UUID examId;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;

}
