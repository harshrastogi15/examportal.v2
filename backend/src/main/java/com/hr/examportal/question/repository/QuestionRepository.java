package com.hr.examportal.question.repository;

import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {


    @Query(value = """
    SELECT q.*
    FROM question q
    JOIN exam e ON q.exam_id = e.id
    WHERE q.id = :questionId
      AND e.creator_id = :userId
    """, nativeQuery = true)
    Optional<Question> findByIdAndUserId(UUID questionId, UUID userId);


    @Query(value = """ 
            WITH opt as (
            SELECT
            ARRAY[
            (SELECT option FROM question_option WHERE question_id = :questionId AND option_label = 'A'),
            (SELECT option FROM question_option WHERE question_id = :questionId AND option_label = 'B'),
            (SELECT option FROM question_option WHERE question_id = :questionId AND option_label = 'C'),
            (SELECT option FROM question_option WHERE question_id = :questionId AND option_label = 'D')
            ] AS option_values,
            ARRAY[
        (SELECT image_url FROM question_option WHERE question_id = :questionId AND option_label = 'A'),
            (SELECT image_url FROM question_option WHERE question_id = :questionId AND option_label = 'B'),
            (SELECT image_url FROM question_option WHERE question_id = :questionId AND option_label = 'C'),
            (SELECT image_url FROM question_option WHERE question_id = :questionId AND option_label = 'D')
            ] AS option_urls
)
    SELECT q.id, q.exam_id, q.question_type, q.question_text, q.question_image_url, q.correct_answer, q.suggested_answer, q.difficulty, q.assigned, q.created_at, q.updated_at,
            (SELECT option_values from opt),
            (SELECT option_urls from opt)
    FROM question q
    JOIN exam e ON q.exam_id = e.id
    WHERE q.id = :questionId
    AND e.creator_id = :userId;
    """, nativeQuery = true)
    List<Object[]> findQuestionDetailsByIdAnsUserId(UUID questionId, UUID userId);
}
