package com.hr.examportal.result.repository;

import com.hr.examportal.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ResultRespository extends JpaRepository<Question, UUID> {

    @Query(
            value = """
                    SELECT sq.id, q.correct_answer, sa.selected_option, ms.mark
                    FROM student_exam_questions sq
                    LEFT JOIN question q ON q.id=sq.question_id
                    LEFT JOIN mark_scheme ms ON ms.exam_id=sq.exam_id and ms.difficulty=q.difficulty
                    LEFT JOIN student_answer sa ON sa.submission_id = sq.id
                    Where sq.exam_assigned_id IN (:assignmentIds) and q.question_type='MCQ'
                    """,
            nativeQuery = true
    )
    List<Object[]> getQuestionAndAnswerDetails(@Param("assignmentIds") List<UUID> assignmentIds);


    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE student_exam_questions
            SET evaluation = :mark
            WHERE id = :submissionId
            """,nativeQuery = true)
    void updateEvaluation(@Param("submissionId") UUID submissionId,@Param("mark") int mark);
}
