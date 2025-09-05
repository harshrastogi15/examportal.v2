package com.hr.examportal.question.repository;

import com.hr.examportal.question.entity.QuestionOption;
import com.hr.examportal.utils.enums.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {

    @Modifying
    @Query(value = "DELETE FROM question_option WHERE question_id=:questionId",nativeQuery = true)
    void deleteByQuestionId(UUID questionId);

    QuestionOption findByQuestionIdAndOptionLabel(UUID questionId, AnswerOption answerOption);
}
