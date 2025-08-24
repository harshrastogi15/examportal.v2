package com.hr.examportal.question.dto;

import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuestionDto {

    @NotNull(message = "Question id must not be null")
    private UUID questionId;

    @NotNull(message = "Exam id must not be null")
    private UUID examId;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    @NotBlank(message = "Question text must not be blank")
    private String questionText;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficulty;

    private List<String> options;


}
