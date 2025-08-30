package com.hr.examportal.question.repository;

import com.hr.examportal.question.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {

}
