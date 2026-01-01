package com.hr.examportal.result.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.assignment.entity.StudentExamAssigned;
import com.hr.examportal.assignment.repository.ExamAssignRepository;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.result.repository.ResultRespository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRespository resultRespository;
    private final ExamAssignRepository examAssignRepository;
    private final IdEncoder idEncoder;
    private final UserId userId;
    public Map<String, Object> calculateResultMCQ(JsonNode json) {

        List<UUID> assignmentIds = new ArrayList<>();
        for (JsonNode node : json.get("assignmentIds")) {
            try {
                assignmentIds.add(idEncoder.decodeId(UUID.fromString(node.asText()),userId.getId()));
            } catch (IllegalArgumentException ex) {
                throw new CustomException("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        }
        List<Object[]> result = resultRespository.getMCQQuestionAndAnswerDetails(assignmentIds);
        String qType = QuestionType.MCQ.toString();
        for (Object[] row : result) {
            UUID submissionId = (UUID) row[0];
            AnswerOption correctAnswer = AnswerOption.valueOf(row[1].toString());
            AnswerOption selectedOption =
                    row[2] == null ? null : AnswerOption.valueOf(row[2].toString());
            int mark = (int) row[3];
            if(selectedOption != null && selectedOption.equals(correctAnswer)){
                resultRespository.updateEvaluation(submissionId,mark,qType);
            }else{
                resultRespository.updateEvaluation(submissionId,0,qType);
            }
        }
        return Map.of("message","Success");
    }

    public Map<String, Object> getSubjectiveQuestions(JsonNode json) {
        List<UUID> assignmentIds = new ArrayList<>();
        for (JsonNode node : json.get("assignmentIds")) {
            try {
                assignmentIds.add(idEncoder.decodeId(UUID.fromString(node.asText()),userId.getId()));
            } catch (IllegalArgumentException ex) {
                throw new CustomException("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        }

        System.out.println(assignmentIds);

        List<Object[]> result = resultRespository.getSubjectiveQuestionAndAnswerDetails(assignmentIds);

        List<Map<String,Object>> questions = new ArrayList<>();

        for(Object[] row : result){
            Map<String,Object> question = new HashMap<>();
            question.put("questionId",idEncoder.encodeId((UUID) row[0],userId.getId()));
            question.put("suggesterAnswer", row[1]);
            question.put("answer", row[2]);
            question.put("maxMark", row[3]);
            question.put("isMarked", row[4]);
            question.put("mark", row[5]);
            questions.add(question);
        }

        return Map.of("message","Success","questions",questions);
    }

    public Map<String, Object> calculateResultSubjective(Map<UUID, Integer> marks) {
        List<Map<String,Object>> response = new ArrayList<>();
        for(Map.Entry<UUID, Integer> entry: marks.entrySet()){
            UUID questionId = idEncoder.decodeId(entry.getKey(),userId.getId());
            Integer mrk = entry.getValue();
            if(resultRespository.isValidMarkQuestion(questionId, QuestionType.Subjective.toString(), mrk)){
                resultRespository.updateEvaluation(questionId,mrk, QuestionType.Subjective.toString());
                response.add(Map.of(entry.getKey().toString(),"Success"));
            }else {
                response.add(Map.of(entry.getKey().toString(),"Error"));
            }
        }

        return Map.of("message",response);
    }

    public Map<String, Object> calculateFinalResult(List<UUID> assignments) {
        List<Map<String,Object>> response = new ArrayList<>();
        for(UUID assignment: assignments){
            UUID assignmentId = idEncoder.decodeId(assignment,userId.getId());
            Optional<StudentExamAssigned> optional = examAssignRepository.findById(assignmentId);
            if (optional.isEmpty()) {
                response.add(Map.of(assignment.toString(), "invalid assignment"));
                continue;
            }
            StudentExamAssigned studentExamAssigned = optional.get();
            if(!studentExamAssigned.getCompletionTime().isBefore(LocalDateTime.now())){
                response.add(Map.of(assignment.toString(), "assignment pending"));
            }
            List<Object[]> result = resultRespository.findAllUserQuestionByAssignmentId(assignmentId);
            boolean flag = true;
            int marks=0;
            for(Object[] row: result){
                if ( row == null || row[0] == null) {
                    flag = false;
                    response.add(Map.of(assignment.toString(), "question evaluation pending"));
                    break;
                }
                int mark = (int) row[0];
                marks += mark;
            }
            if(flag){
                studentExamAssigned.setScore(marks);
                examAssignRepository.save(studentExamAssigned);
                response.add(Map.of(assignment.toString(), marks));
            }
        }
        return Map.of("message",response);
    }
}
