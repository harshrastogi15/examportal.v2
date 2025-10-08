package com.hr.examportal.assignment.repository;

import com.hr.examportal.assignment.entity.StudentExamAssigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExamAssignRepository extends JpaRepository<StudentExamAssigned, UUID> {

    Integer deleteByStudentIdAndExamId(UUID studentId, UUID examId);

    StudentExamAssigned findByStudentIdAndExamId(UUID studentId, UUID examId);

    List<StudentExamAssigned> findAllByExamId(UUID examId);

    @Query(value = "SELECT ea.id, ea.status, e.title, e.description, e.duration_minutes, e.total_marks, e.start_time, e.end_time " +
                   "FROM student_exam_assigned ea LEFT JOIN exam e " +
                   "ON ea.exam_id=e.id " +
                   "WHERE ea.student_id=:id "
            ,nativeQuery = true)
    List<Object[]> findAllByUserId(UUID id);
}
