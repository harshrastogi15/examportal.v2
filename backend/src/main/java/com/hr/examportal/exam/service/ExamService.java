package com.hr.examportal.exam.service;

import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.entity.Exam;
import com.hr.examportal.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final ModelMapper mapper;
    public void createExam(CreateExamDto dto) {
        Exam exam = mapper.map(dto, Exam.class);
        exam.setDurationMinutes(LocalTime.parse("00:30:00"));
        examRepository.save(exam);
    }
}
