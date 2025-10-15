package com.hr.examportal.attempt.service;

import com.hr.examportal.assignment.entity.StudentExamAssigned;
import com.hr.examportal.assignment.entity.StudentExamQuestion;
import com.hr.examportal.assignment.repository.ExamAssignRepository;
import com.hr.examportal.assignment.repository.QuestionAssignRepository;
import com.hr.examportal.exam.entity.Exam;
import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.StatusStage;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AttemptService {
    private final ExamAssignRepository examAssignRepository;
    private final QuestionAssignRepository questionAssignRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final IdEncoder idEncoder;
    private final UserId userId;


    private void getAllQuestionIdInternal(UUID assignmentId, List<UUID> questionList) {
        List<UUID> questionIdList = questionAssignRepository.findAllByExamAssignedId(assignmentId);
        for (UUID id : questionIdList) {
            questionList.add(idEncoder.encodeId(id, userId.getId()));
        }
    }

    @Transactional
    public Map<String, Object> getAllQuestionId(UUID assignmentId) {
        LocalDateTime entryTime = LocalDateTime.now();
        assignmentId = idEncoder.decodeId(assignmentId,userId.getId());
        StudentExamAssigned studentExamAssigned = examAssignRepository.findById(assignmentId).orElseThrow(
                ()-> new CustomException("NOT Performed", HttpStatus.NOT_FOUND)
        );
        if(!studentExamAssigned.getStudentId().equals(userId.getId())){
            throw new CustomException("NOT Authorized", HttpStatus.UNAUTHORIZED);
        }

        List<UUID> questionList = new ArrayList<>();
        switch (studentExamAssigned.getStatus()){
            case Completed, Result:
                throw new CustomException("Already Attempted", HttpStatus.LOCKED);
            case Generated:
                isExamSessionValid(studentExamAssigned);
                getAllQuestionIdInternal(assignmentId,questionList);
                break;
            case Pending:
                generateQuestionsAndStartTest(assignmentId, studentExamAssigned, entryTime, questionList);
                break;
        }
        return Map.of("assignmentId",idEncoder.encodeId(assignmentId,userId.getId()),
                "startTime",studentExamAssigned.getStartTime(),
                "endTime",studentExamAssigned.getCompletionTime(),
                "questionArray",questionList);

    }

    private void isExamSessionValid(StudentExamAssigned studentExamAssigned) {
        LocalDateTime entryTime = LocalDateTime.now();
        if (entryTime.isBefore(studentExamAssigned.getStartTime()) || entryTime.isAfter(studentExamAssigned.getCompletionTime())) {
            studentExamAssigned.setStatus(StatusStage.Completed);
            examAssignRepository.save(studentExamAssigned);
            throw new CustomException("Time passed", HttpStatus.BAD_REQUEST);
        }
    }

    private void generateQuestionsAndStartTest(UUID assignmentId, StudentExamAssigned studentExamAssigned, LocalDateTime entryTime, List<UUID> questionList) {
        Exam exam = examRepository.findById(studentExamAssigned.getExamId()).orElseThrow(
                ()-> new CustomException("Something is wrong")
        );
        if (entryTime.isBefore(exam.getStartTime()) || entryTime.isAfter(exam.getEndTime())) {
            throw new CustomException("Invalid time", HttpStatus.BAD_REQUEST);
        }
        studentExamAssigned.setStatus(StatusStage.Generated);
        examAssignRepository.save(studentExamAssigned);

        // generate Question
        generateQuestion(assignmentId,exam.getId(),exam.getNoQuestionPerLevel());
        getAllQuestionIdInternal(assignmentId,questionList);

        LocalTime durationTime = exam.getDurationMinutes();
        Duration duration = Duration.ofHours(durationTime.getHour())
                .plusMinutes(durationTime.getMinute());

        studentExamAssigned.setStartTime(LocalDateTime.now());
        studentExamAssigned.setCompletionTime(LocalDateTime.now().plus(duration));

        examAssignRepository.save(studentExamAssigned);
    }

    private void generateQuestion(UUID assignmentId, UUID examId, List<Integer> noQuestionPerLevel) {

        for(int i=0;i<4;i++){
           List<UUID> questionIdList = questionRepository.findByExamIdAndDifficulty(examId, DifficultyLevel.values()[i].toString());
           if(questionIdList.size()<noQuestionPerLevel.get(i)){
               throw new CustomException("Invalid exam", HttpStatus.CONFLICT);
           }
           Collections.shuffle(questionIdList);
           for(int j=0;j<noQuestionPerLevel.get(i);j++){
               StudentExamQuestion stQuestion = StudentExamQuestion.builder()
                       .questionId(questionIdList.get(j))
                       .examId(examId)
                       .examAssignedId(assignmentId)
                       .build();
               questionAssignRepository.save(stQuestion);
           }
        }
    }

}
