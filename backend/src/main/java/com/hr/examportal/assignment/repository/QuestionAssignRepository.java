package com.hr.examportal.assignment.repository;

import com.hr.examportal.assignment.entity.StudentExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestionAssignRepository extends JpaRepository<StudentExamQuestion, UUID> {


    @Query(value = "SELECT id from student_exam_questions where exam_assigned_id=:assignmentId",nativeQuery = true)
    List<UUID> findAllByExamAssignedId(UUID assignmentId);
}
