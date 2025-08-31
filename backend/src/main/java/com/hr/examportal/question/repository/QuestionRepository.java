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


    @Query(value = "SELECT q.*" +
            "    FROM question q" +
            "    JOIN exam e ON q.exam_id = e.id" +
            "    WHERE q.id = :questionId" +
            "    AND e.creator_id = :userId", nativeQuery = true)
    List<Object[]> findQuestionDetailsByIdAnsUserId(UUID questionId, UUID userId);
}
