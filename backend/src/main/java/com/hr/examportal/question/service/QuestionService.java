package com.hr.examportal.question.service;

import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.question.dto.CreateQuestionDto;
import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.entity.Question;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
    private final TokenUtil tokenUtil;
    private final String imageURLEndPoint="localhost:8080/api/image/";

    public ReadQuestionInstructor createQuestion(CreateQuestionDto dto) {
        Question question = mapper.map(dto,Question.class);
        question.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        questionRepository.save(question);
        return getQuestionDetailsInternal(question.getId());
    }

    private ReadQuestionInstructor getQuestionDetailsInternal(UUID questionId){
        ReadQuestionInstructor readQuestionInstructorDto= new ReadQuestionInstructor();
        List<Object[]> result = questionRepository.findQuestionDetailsByIdAnsUserId(questionId,userId.getId());
        if(!result.isEmpty()){
            Object[] row = result.get(0);
            readQuestionInstructorDto = ReadQuestionInstructor.builder()
                    .questionId(idEncoder.encodeId((UUID) row[0],userId.getId()))
                    .examId(idEncoder.encodeId((UUID) row[1],userId.getId()))
                    .questionType(QuestionType.valueOf(row[2].toString()))
                    .questionText((String) row[3])
                    .questionImageUrl((String) row[4])
                    .correctAnswer(AnswerOption.valueOf(row[5].toString()))
                    .suggestedAnswer(Arrays.asList((String[]) row[6]))
                    .difficulty(DifficultyLevel.valueOf(row[7].toString()))
                    .isAssigned((Boolean) row[8])
                    .createdAt(((Timestamp) row[9]).toLocalDateTime())
                    .updatedAt(((Timestamp) row[10]).toLocalDateTime())
                    .options(Arrays.asList((String[]) row[11]))
                    .optionsUrl(Arrays.asList((String[]) row[12]))
                    .build();

            FileMetadata payload = FileMetadata.builder()
                    .questionId(readQuestionInstructorDto.getQuestionId())
                    .examId(readQuestionInstructorDto.getExamId())
                    .build();
            if(readQuestionInstructorDto.getQuestionImageUrl()!=null && !readQuestionInstructorDto.getQuestionImageUrl().isEmpty()){
                payload.setLocation(readQuestionInstructorDto.getQuestionImageUrl());
                readQuestionInstructorDto.setQuestionImageUrl(imageURLEndPoint + tokenUtil.generateToken(payload));
            }
        }else{
            throw new CustomException("Invalid id");
        }

        return readQuestionInstructorDto;
    }

    public ReadQuestionInstructor getQuestionDetailsInstructor(UUID questionId){
        return getQuestionDetailsInternal(idEncoder.decodeId(questionId,userId.getId()));
    }

}
