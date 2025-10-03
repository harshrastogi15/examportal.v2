package com.hr.examportal.exam.repository;


import com.hr.examportal.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.*;

public interface ExamRepository extends JpaRepository<Exam, UUID> {
    @Query(value = "WITH easy_mark AS ( " +
            "    SELECT mark FROM mark_scheme WHERE difficulty='Easy' AND exam_id = :examId), " +
            "medium_mark AS ( " +
            "    SELECT mark FROM mark_scheme WHERE difficulty='Medium' AND exam_id = :examId), " +
            "hard_mark AS ( " +
            "    SELECT mark FROM mark_scheme WHERE difficulty='Hard' AND exam_id = :examId), " +
            "sub_mark AS ( " +
            "    SELECT mark FROM mark_scheme WHERE difficulty='Subjective' AND exam_id = :examId) " +
            "SELECT e.id, e.title, e.description, e.instructions, e.duration_minutes, e.total_marks, e.no_question_per_level, " +
            "       e.start_time, e.end_time, e.created_at, e.updated_at, u.name, " +
            "       (SELECT mark FROM easy_mark) AS easy_mark, " +
            "       (SELECT mark FROM medium_mark) AS medium_mark, " +
            "       (SELECT mark FROM hard_mark) AS hard_mark, " +
            "       (SELECT mark FROM sub_mark) AS sub_mark, " +
            "       e.is_ready " +
            "FROM exam e " +
            "JOIN users u ON e.creator_id = u.id " +
            "WHERE e.id = :examId", nativeQuery = true)
    List<Object[]> findExamById(UUID examId);


    @Query(value = "SELECT e.id, e.title, e.description, e.duration_minutes, e.total_marks FROM exam e WHERE e.creator_id = :userId",nativeQuery = true)
    List<Object[]> findAllExam(UUID userId);

    Optional<Exam> findByIdAndCreatorId(UUID examId, UUID creatorId);

    @Query(value = "SELECT e.is_ready FROM exam e WHERE e.id=:examId", nativeQuery = true)
    boolean isExamReady(UUID examId);
}
