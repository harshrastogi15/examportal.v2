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

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
    public ReadQuestionInstructor createQuestion(CreateQuestionDto dto) {
        ReadQuestionInstructor rdto = new ReadQuestionInstructor();
        Question question = mapper.map(dto,Question.class);
        question.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        System.out.println(question.getExamId());
        System.out.println(question.getQuestionType());
        System.out.println(question.getQuestionText());
        questionRepository.save(question);
        return rdto;
    }
}
