package com.hr.examportal.assignment.repository;

import com.hr.examportal.assignment.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, UUID> {
    Optional<StudentAnswer> findBySubmissionId(UUID submissionId);
}
