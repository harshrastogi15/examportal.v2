package com.hr.examportal.attempt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.assignment.entity.StudentAnswer;
import com.hr.examportal.assignment.entity.StudentExamAssigned;
import com.hr.examportal.assignment.entity.StudentExamQuestion;
import com.hr.examportal.assignment.repository.ExamAssignRepository;
import com.hr.examportal.assignment.repository.QuestionAssignRepository;
import com.hr.examportal.assignment.repository.StudentAnswerRepository;
import com.hr.examportal.exam.entity.Exam;
import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.question.dto.ReadQuestionStudent;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.ImageUrlGenerator;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AttemptService {
    private final ExamAssignRepository examAssignRepository;
    private final QuestionAssignRepository questionAssignRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final IdEncoder idEncoder;
    private final UserId userId;
    private final TokenUtil tokenUtil;


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
                if(!isExamSessionValid(studentExamAssigned)) return Map.of("message","Time Passed");
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

    private boolean isExamSessionValid(StudentExamAssigned studentExamAssigned) {
        LocalDateTime entryTime = LocalDateTime.now();
        if (entryTime.isBefore(studentExamAssigned.getStartTime()) || entryTime.isAfter(studentExamAssigned.getCompletionTime())) {
            studentExamAssigned.setStatus(StatusStage.Completed);
            examAssignRepository.save(studentExamAssigned);
            System.out.println(studentExamAssigned.getStatus());
            return false;
        }
        return true;
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

    private ReadQuestionStudent getQuestionDetailsInternal(UUID questionId, UUID assignmentId, UUID userQuestionId){
        ReadQuestionStudent readQuestionStudentDto;
        List<Object[]> result = questionRepository.findQuestionDetailsForStudentById(questionId);

        if(!result.isEmpty()){
            Object[] row = result.get(0);
            if(QuestionType.valueOf(row[0].toString()).equals(QuestionType.MCQ)){
                readQuestionStudentDto = ReadQuestionStudent.builder()
                        .questionId(userQuestionId)
                        .examId(assignmentId)
                        .questionType(QuestionType.valueOf(row[0].toString()))
                        .questionText((String) row[1])
                        .questionImageUrl((String) row[2])
                        .difficulty(DifficultyLevel.valueOf(row[3].toString()))
                        .options(Arrays.asList((String[]) row[4]))
                        .optionsUrl(Arrays.asList((String[]) row[5]))
                        .build();
                FileMetadata optionPayload = FileMetadata.builder()
                        .questionId(readQuestionStudentDto.getQuestionId())
                        .examId(readQuestionStudentDto.getExamId())
                        .build();

                for(int i=0;i<4;i++){
                    if(readQuestionStudentDto.getOptionsUrl().get(i)!=null){
                        optionPayload.setLocation(readQuestionStudentDto.getOptionsUrl().get(i));
                        readQuestionStudentDto.getOptionsUrl().set(i, ImageUrlGenerator.getUrl(tokenUtil.generateToken(optionPayload)));
                    }
                }

            }else{
                readQuestionStudentDto = ReadQuestionStudent.builder()
                        .questionId(userQuestionId)
                        .examId(assignmentId)
                        .questionType(QuestionType.valueOf(row[0].toString()))
                        .questionText((String) row[1])
                        .questionImageUrl((String) row[2])
                        .difficulty(DifficultyLevel.valueOf(row[3].toString()))
                        .build();
            }

            FileMetadata payload = FileMetadata.builder()
                    .questionId(readQuestionStudentDto.getQuestionId())
                    .examId(readQuestionStudentDto.getExamId())
                    .build();
            if(readQuestionStudentDto.getQuestionImageUrl()!=null && !readQuestionStudentDto.getQuestionImageUrl().isEmpty()){
                payload.setLocation(readQuestionStudentDto.getQuestionImageUrl());
                readQuestionStudentDto.setQuestionImageUrl(ImageUrlGenerator.getUrl(tokenUtil.generateToken(payload)));
            }
        }else{
            throw new CustomException("Invalid id");
        }

        return readQuestionStudentDto;
    }



    public ReadQuestionStudent getQuestion(UUID assignmentId, UUID userQuestionId) {

        StudentExamAssigned studentExamAssigned = examAssignRepository.findById(idEncoder.decodeId(assignmentId,userId.getId())).orElseThrow(
                ()-> new CustomException("NOT Performed", HttpStatus.NOT_FOUND)
        );
        if(!studentExamAssigned.getStudentId().equals(userId.getId())){
            throw new CustomException("NOT Authorized", HttpStatus.UNAUTHORIZED);
        }

        if(!isExamSessionValid(studentExamAssigned)){
            throw new CustomException("Invalid session",HttpStatus.BAD_REQUEST);
        }

        UUID questionId = questionAssignRepository.findQuestionIdByExamAssignedIdAndId(idEncoder.decodeId(assignmentId,userId.getId()),idEncoder.decodeId(userQuestionId,userId.getId()))
                .orElseThrow(()-> new CustomException("invalid ids"));
        return getQuestionDetailsInternal(questionId,assignmentId,userQuestionId);
    }

    public Map<String,Object> storeAnswer(UUID assignmentId, UUID userQuestionId, JsonNode json) {

        StudentExamAssigned studentExamAssigned = examAssignRepository.findById(idEncoder.decodeId(assignmentId,userId.getId())).orElseThrow(
                ()-> new CustomException("NOT Performed", HttpStatus.NOT_FOUND)
        );
        if(!studentExamAssigned.getStudentId().equals(userId.getId())){
            throw new CustomException("NOT Authorized", HttpStatus.UNAUTHORIZED);
        }

        if(!isExamSessionValid(studentExamAssigned)){
            throw new CustomException("Invalid session",HttpStatus.BAD_REQUEST);
        }


        UUID decodedUserQuestionId = idEncoder.decodeId(userQuestionId,userId.getId());
        Optional<StudentAnswer> existingOpt = studentAnswerRepository.findBySubmissionId(decodedUserQuestionId);
        StudentAnswer studentAnswer = existingOpt.orElseGet(StudentAnswer::new);

        studentAnswer.setSubmissionId(decodedUserQuestionId);
        studentAnswer.setMode(AnswerSelection.valueOf(json.get("selection").asText()));
        studentAnswer.setSelectedOption(AnswerOption.valueOf(json.get("option").asText()));
        studentAnswer.setSubjectiveAnswer(json.get("text").asText());

        studentAnswerRepository.save(studentAnswer);
        return Map.of("status","success");
    }
}
