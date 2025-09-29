package com.hr.examportal.exam.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hr.examportal.utils.StrictIntegerDeserializer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadExamDto {

    private UUID examId;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private List<String> instructions;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Total marks are required")
    @Min(value = 1, message = "Total marks must be at least 1")
    private Integer totalMarks;

    @JsonDeserialize(contentUsing = StrictIntegerDeserializer.class)
    @NotNull(message = "Questions per level must be provided")
    @Size(min = 4, max = 4, message = "Exactly 4 question levels are required")
    private List<Integer> noQuestionPerLevel;

    @NotNull(message = "Start time must not be null")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Easy Level Question Mark must not be null")
    private Integer easyLevelMark;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Medium Level Question Mark must not be null")
    private Integer mediumLevelMark;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Hard Level Question Mark must not be null")
    private Integer hardLevelMark;

    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    @NotNull(message = "Subjective Level Question Mark must not be null")
    private Integer subLevelMark;

    private Boolean isReady;

    @Column(nullable = false, updatable = false)
    private String creator;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}

