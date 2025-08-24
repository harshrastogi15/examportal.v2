package com.hr.examportal.question.dto;

import com.hr.examportal.utils.enums.AnswerOption;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReadQuestionInstructor extends QuestionDto{

    private String questionImageUrl;

    private AnswerOption correctAnswer;

    private List<String> suggestedAnswer;

    private List<String> optionsUrl;
}
