package com.hr.examportal.exam.service;

import com.hr.examportal.exam.dto.CreateExamDto;
import com.hr.examportal.exam.dto.ReadExamDto;
import com.hr.examportal.exam.entity.Exam;
import com.hr.examportal.exam.entity.MarkScheme;
import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exam.repository.MarkSchemeRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.DifficultyLevel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final MarkSchemeRepository markSchemeRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;

    @Transactional
    public ReadExamDto createExam(CreateExamDto dto) {
        Exam exam = mapper.map(dto, Exam.class);
        Integer minutes = dto.getDurationMinutes();
        LocalTime time = LocalTime.of(minutes / 60, minutes % 60);
        exam.setDurationMinutes(time);
        exam.setCreatorId(userId.getId());
        examRepository.save(exam);
        MarkScheme mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Easy).mark(dto.getEasyLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Medium).mark(dto.getMediumLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Hard).mark(dto.getHardLevelMark()).build();
        markSchemeRepository.save(mark);
        mark = MarkScheme.builder().examId(exam.getId()).difficulty(DifficultyLevel.Subjective).mark(dto.getSubLevelMark()).build();
        markSchemeRepository.save(mark);
        ReadExamDto rdto = getExamDetailsInternal(exam.getId());
        return rdto;
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
}
