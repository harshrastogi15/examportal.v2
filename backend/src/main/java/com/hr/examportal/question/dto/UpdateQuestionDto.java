package com.hr.examportal.question.dto;

import com.hr.examportal.utils.enums.AnswerOption;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateQuestionDto extends QuestionDto{

    @NotNull(message = "provide correct answer")
    private AnswerOption correctAnswer;

    private List<String> suggestedAnswer;
}
