package com.hr.examportal.question.service;

import com.hr.examportal.question.dto.CreateQuestionDto;
import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.entity.Question;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.UserId;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
    public ReadQuestionInstructor createQuestion(CreateQuestionDto dto) {
        Question question = mapper.map(dto,Question.class);
        question.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        questionRepository.save(question);
        return getQuestionDetailsInternal(question.getId());
    }

    private ReadQuestionInstructor getQuestionDetailsInternal(UUID questionId){
        ReadQuestionInstructor readQuestionInstructor= new ReadQuestionInstructor();
        List<Object[]> result = questionRepository.findQuestionDetailsByIdAnsUserId(questionId,userId.getId());
        readQuestionInstructor.setQuestionId(idEncoder.encodeId(questionId,userId.getId()));
        return readQuestionInstructor;
    }



}
