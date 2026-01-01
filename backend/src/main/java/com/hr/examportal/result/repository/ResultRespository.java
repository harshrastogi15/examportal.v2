package com.hr.examportal.result.repository;

import com.hr.examportal.question.entity.Question;
import com.hr.examportal.utils.enums.QuestionType;
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
    List<Object[]> getMCQQuestionAndAnswerDetails(@Param("assignmentIds") List<UUID> assignmentIds);


    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE student_exam_questions
            SET evaluation = :mark
            WHERE id = :submissionId
            """,nativeQuery = true)
    void updateEvaluation(@Param("submissionId") UUID submissionId,@Param("mark") int mark, @Param("qType") String qType);


    @Query(
            value = """
                    SELECT sq.id, q.suggested_answer, sa.subjective_answer, ms.mark, CASE WHEN sq.evaluation IS NULL THEN 0 ELSE 1 END as is_marked, sq.evaluation
                    FROM student_exam_questions sq
                    LEFT JOIN question q ON q.id=sq.question_id
                    LEFT JOIN mark_scheme ms ON ms.exam_id=sq.exam_id and ms.difficulty=q.difficulty
                    LEFT JOIN student_answer sa ON sa.submission_id = sq.id
                    Where sq.exam_assigned_id IN (:assignmentIds) and q.question_type='Subjective'
                    """,
            nativeQuery = true
    )
    List<Object[]> getSubjectiveQuestionAndAnswerDetails(@Param("assignmentIds") List<UUID> assignmentIds);

    @Query(value = """
            SELECT CASE WHEN COUNT(sq) > 0 THEN true ELSE false END
            FROM student_exam_questions sq
            LEFT JOIN question q ON q.id=sq.question_id
            LEFT JOIN mark_scheme ms ON ms.exam_id=sq.exam_id and ms.difficulty=q.difficulty
            WHERE sq.id = :id
            AND q.question_type = (:qType)::question_type_enum
            AND :mark<=ms.mark
            """, nativeQuery = true)
    boolean isValidMarkQuestion(@Param("id") UUID id,
                            @Param("qType") String qType, @Param("mark") Integer mark);

    @Query(value = """
            SELECT sq.evaluation
            FROM student_exam_questions sq
            WHERE sq.exam_assigned_id = :assignmentId
            """, nativeQuery = true)
    List<Object[]> findAllUserQuestionByAssignmentId(@Param("assignmentId") UUID assignmentId);
}
