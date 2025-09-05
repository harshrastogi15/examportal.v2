package com.hr.examportal.question.dto;

import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionDto {
    @NotNull(message = "Exam id must not be null")
    private UUID examId;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private DifficultyLevel difficulty=DifficultyLevel.Easy;

    private String questionText = "New Question";

    private List<String> suggestedAnswer=new ArrayList<>();
}

