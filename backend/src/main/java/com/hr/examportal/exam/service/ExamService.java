package com.hr.examportal.exam.service;

import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.dto.ReadExamDto;
import com.hr.examportal.exam.entity.Exam;
import com.hr.examportal.exam.entity.MarkScheme;
import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exam.repository.MarkSchemeRepository;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.question.service.QuestionService;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final MarkSchemeRepository markSchemeRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
    private final QuestionService questionService;

    @Transactional
    public ReadExamDto createExam(CreateExamDto dto) {
        Exam exam = mapper.map(dto, Exam.class);
        Integer minutes = dto.getDurationMinutes();
        LocalTime time = LocalTime.of(minutes / 60, minutes % 60);
        exam.setDurationMinutes(time);
        exam.setCreatorId(userId.getId());
        exam.setIsReady(false);
        examRepository.save(exam);
        MarkScheme mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Easy).mark(dto.getEasyLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Medium).mark(dto.getMediumLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Hard).mark(dto.getHardLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Subjective).mark(dto.getSubLevelMark()).build();
        markSchemeRepository.save(mark);
        return getExamDetailsInternal(exam.getId());
    }

    private ReadExamDto getExamDetailsInternal(UUID examId){
        ReadExamDto dto = new ReadExamDto();
        List<Object[]> result = examRepository.findExamById(examId);
        if (!result.isEmpty()) {
            Object[] row = result.get(0);
            Integer totalMinutes = ((java.sql.Time) row[4]).toLocalTime().getHour() * 60 + ((java.sql.Time) row[4]).toLocalTime().getMinute();
            dto = ReadExamDto.builder()
                    .examId(idEncoder.encodeId(examId,userId.getId()))
                    .title((String) row[1])
                    .description((String) row[2])
                    .instructions(Arrays.asList((String[]) row[3]))
                    .durationMinutes(totalMinutes)
                    .totalMarks((Integer) row[5])
                    .noQuestionPerLevel(Arrays.asList((Integer[]) row[6]))
                    .startTime(((Timestamp) row[7]).toLocalDateTime())
                    .endTime(((Timestamp) row[8]).toLocalDateTime())
                    .createdAt(((Timestamp) row[9]).toLocalDateTime())
                    .updatedAt(((Timestamp) row[10]).toLocalDateTime())
                    .creator((String) row[11])
                    .easyLevelMark((Integer) row[12])
                    .mediumLevelMark((Integer) row[13])
                    .hardLevelMark((Integer) row[14])
                    .subLevelMark((Integer) row[15])
                    .isReady((Boolean) row[16])
                    .build();
        } else {
            throw new NoSuchElementException("Unable to get exam details");
        }
        return dto;
    }

    public ReadExamDto getExamDetails(UUID examId){
        UUID realId = idEncoder.decodeId(examId,userId.getId());
        return getExamDetailsInternal(realId);
    }

    public List<Map<String, Object>> getAllExam() {
        List<Map<String,Object>> exams = new ArrayList<>();
        List<Object[]> result = examRepository.findAllExam(userId.getId());
        for(Object[] row : result){
            Map<String,Object> exam = new HashMap<>();
            exam.put("examId",idEncoder.encodeId((UUID) row[0],userId.getId()));
            exam.put("title", row[1]);
            exam.put("description", row[2]);
            exam.put("durationMinutes", row[3]);
            exam.put("totalMarks", row[4]);
            exams.add(exam);
        }
        return exams;
    }

    @Transactional
    public ReadExamDto updateExam(UUID examId, CreateExamDto dto) {
        examId = idEncoder.decodeId(examId,userId.getId());
        Exam existingExam = examRepository.findByIdAndCreatorId(examId,userId.getId())
                .orElseThrow(() -> new NoSuchElementException("Unable to get exam details"));
        if(existingExam.getIsReady().equals(true)){
            if(!existingExam.getNoQuestionPerLevel().equals(dto.getNoQuestionPerLevel())){
                throw new CustomException("As exam is ready to be assigned, cant update question no.");
            }
        }
        mapper.map(dto, existingExam);
        examRepository.save(existingExam);

        MarkScheme easy = markSchemeRepository.findByExamIdAndDifficulty(examId,DifficultyLevel.Easy);
        MarkScheme medium = markSchemeRepository.findByExamIdAndDifficulty(examId,DifficultyLevel.Medium);
        MarkScheme hard = markSchemeRepository.findByExamIdAndDifficulty(examId,DifficultyLevel.Hard);
        MarkScheme subjective = markSchemeRepository.findByExamIdAndDifficulty(examId,DifficultyLevel.Subjective);
        easy.setMark(dto.getEasyLevelMark());
        medium.setMark(dto.getMediumLevelMark());
        hard.setMark(dto.getHardLevelMark());
        subjective.setMark((dto.getSubLevelMark()));
        markSchemeRepository.save(easy);
        markSchemeRepository.save(medium);
        markSchemeRepository.save(hard);
        markSchemeRepository.save(subjective);
        return getExamDetailsInternal(examId);
    }

    public Void examReady(UUID examId) {
        examId = idEncoder.decodeId(examId,userId.getId());
        Exam exam = examRepository.findByIdAndCreatorId(examId,userId.getId())
                .orElseThrow(() -> new NoSuchElementException("Unable to get exam details"));
        if(checkExamReady(examId,exam.getNoQuestionPerLevel()))
            exam.setIsReady(true);
        else
            throw new CustomException("No of Question should be present in set");
        examRepository.save(exam);
        return null;
    }

    private boolean checkExamReady(UUID examId, List<Integer> noQuestionPerLevel) {
        for(int i=0;i<4;i++){
            int cnt = questionService.countQuestionByLevelAndExamId(examId,DifficultyLevel.values()[i]);
            if(noQuestionPerLevel.get(i) > cnt)
                return false;
        }
        return true;
    }

}
