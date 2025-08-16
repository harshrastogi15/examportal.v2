package com.hr.examportal.exam.repository;


import com.hr.examportal.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ExamRepository extends JpaRepository<Exam, UUID> {

}
