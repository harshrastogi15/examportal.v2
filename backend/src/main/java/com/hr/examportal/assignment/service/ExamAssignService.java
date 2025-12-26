package com.hr.examportal.assignment.service;

import com.hr.examportal.assignment.dto.ExamAssignDto;
import com.hr.examportal.assignment.entity.StudentExamAssigned;
import com.hr.examportal.assignment.repository.ExamAssignRepository;
import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.user.repository.UserRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.StatusStage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamAssignService {
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final IdEncoder idEncoder;
    private final UserId userId;
    private final ModelMapper modelMapper;
    private final ExamAssignRepository examAssignRepository;

    public Map<String, String> assignExam(ExamAssignDto dto) {
        dto.setStudentId(idEncoder.decodeId(dto.getStudentId(),userId.getId()));
        dto.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        boolean isExamValid = examRepository.isExamReady(dto.getExamId()).orElseThrow(
                ()-> new CustomException("Exam is not valid")
        );
        boolean isUserValid = userRepository.isUserPresent(dto.getStudentId()).orElseThrow(
                ()-> new CustomException("Student not found")
        );
        if(!isExamValid || !isUserValid){
            throw new CustomException("Either User is invalid or exam is not ready");
        }

        StudentExamAssigned studentExamAssigned = modelMapper.map(dto,StudentExamAssigned.class);
        studentExamAssigned.setCreatorId(userId.getId());
        examAssignRepository.save(studentExamAssigned);

        return Map.of("message","SUCCESS");
    }

    @Transactional
    public Map<String, String> deleteAssignedExam(ExamAssignDto dto) {
        dto.setStudentId(idEncoder.decodeId(dto.getStudentId(),userId.getId()));
        dto.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        StudentExamAssigned studentExamAssigned = examAssignRepository.findByStudentIdAndExamId(dto.getStudentId(),dto.getExamId());

        if(!studentExamAssigned.getStatus().equals(StatusStage.Pending)){
            throw new CustomException("Student has started assignment or already submitted", HttpStatus.CONFLICT);
        }

        Integer deletedCount = examAssignRepository.deleteByStudentIdAndExamId(dto.getStudentId(),dto.getExamId());
        if(deletedCount==0){
            throw new CustomException("Record Not Found", HttpStatus.NOT_FOUND);
        }
        return Map.of("message","DELETED");
    }

    public List<Map<String, Object>> getAllUsersAssigned(UUID examId) {
        List<StudentExamAssigned> examAssignedList = examAssignRepository.findAllByExamId(idEncoder.decodeId(examId,userId.getId()));
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (StudentExamAssigned assignment : examAssignedList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("studentId", idEncoder.encodeId(assignment.getStudentId(),userId.getId()));
            resultMap.put("assignmentId", idEncoder.encodeId(assignment.getId(),userId.getId()));
            resultMap.put("examId", idEncoder.encodeId(assignment.getExamId(),userId.getId()));
            resultMap.put("status", assignment.getStatus());
            resultMap.put("score", assignment.getScore());
            resultMap.put("startTime", assignment.getStartTime());
            resultMap.put("completionTime", assignment.getCompletionTime());
            resultMap.put("assignAt", assignment.getAssignAt());
            resultList.add(resultMap);
        }

        return resultList;
    }

    public List<Map<String, Object>> getAllAssignment() {
        UUID id = userId.getId();
        List<Object[]> assignmentList = examAssignRepository.findAllByUserId(id);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for(Object[] row : assignmentList){
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("assignmentId",idEncoder.encodeId((UUID) row[0],id));
            resultMap.put("status",(String) row[1]);
            resultMap.put("title",(String) row[2]);
            resultMap.put("description",(String) row[3]);
            Integer totalMinutes = ((java.sql.Time) row[4]).toLocalTime().getHour() * 60 + ((java.sql.Time) row[4]).toLocalTime().getMinute();
            resultMap.put("durationMinutes",totalMinutes);
            resultMap.put("TotalMarks",(Integer) row[5]);
            resultMap.put("WindowStartTime",((Timestamp) row[6]).toLocalDateTime());
            resultMap.put("WindowEndTime",((Timestamp) row[7]).toLocalDateTime());

            resultList.add(resultMap);
        }

        return resultList;
    }

}
