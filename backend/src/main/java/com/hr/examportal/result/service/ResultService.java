package com.hr.examportal.result.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.result.repository.ResultRespository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.AnswerOption;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRespository resultRespository;
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
        List<Object[]> result = resultRespository.getQuestionAndAnswerDetails(assignmentIds);

        for (Object[] row : result) {
            UUID submissionId = (UUID) row[0];
            AnswerOption correctAnswer = AnswerOption.valueOf(row[1].toString());
            AnswerOption selectedOption =
                    row[2] == null ? null : AnswerOption.valueOf(row[2].toString());
            int mark = (int) row[3];
            if(selectedOption != null && selectedOption.equals(correctAnswer)){
                resultRespository.updateEvaluation(submissionId,mark);
            }else{
                resultRespository.updateEvaluation(submissionId,0);
            }
        }
        return Map.of("message","Success");
    }
}
