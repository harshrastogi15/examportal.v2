package com.hr.examportal.exam.repository;

import com.hr.examportal.exam.entity.MarkScheme;
import com.hr.examportal.utils.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarkSchemeRepository extends JpaRepository<MarkScheme, UUID> {
    MarkScheme findByExamIdAndDifficulty(UUID examId, DifficultyLevel difficultyLevel);

//    @Query("SELECT m FROM MarkScheme m JOIN Exam e ON e.id = m.examId WHERE e.id = :examId")
//    List<MarkScheme> findMarkSchemesByExamId(@Param("examId") UUID examId);
}
