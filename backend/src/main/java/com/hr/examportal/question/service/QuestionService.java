package com.hr.examportal.question.service;

import com.hr.examportal.exam.repository.ExamRepository;
import com.hr.examportal.exam.service.ExamService;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.image.service.ImageService;
import com.hr.examportal.question.dto.CreateQuestionDto;
import com.hr.examportal.question.dto.ReadQuestionInstructor;
import com.hr.examportal.question.dto.UpdateQuestionDto;
import com.hr.examportal.question.entity.Question;
import com.hr.examportal.question.entity.QuestionOption;
import com.hr.examportal.question.repository.QuestionOptionRepository;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.ImageUrlGenerator;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.DifficultyLevel;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final ModelMapper mapper;
    private final IdEncoder idEncoder;
    private final UserId userId;
    private final TokenUtil tokenUtil;
    private final ImageService imageService;
    private final ExamRepository examRepository;

    private void generateQuestionOption(UUID questionId){
        for(int i = 0; i<AnswerOption.values().length; i++){
            QuestionOption option = QuestionOption.builder()
                .questionId(questionId)
                .optionLabel(AnswerOption.values()[i])
                .build();
            questionOptionRepository.save(option);
        }
    }

    private void deleteQuestionOption(UUID questionId,UUID examId){
        List<QuestionOption> questionOptions = questionOptionRepository.findAllByQuestionId(questionId);
        questionOptions.forEach((e)->{
            if(e.getImageUrl()!=null){
                imageService.deleteFile(examId,questionId,e.getImageUrl());
            }
        });
        questionOptionRepository.deleteByQuestionId(questionId);
    }
    @Transactional
    public ReadQuestionInstructor createQuestion(CreateQuestionDto dto) {
        Question question = mapper.map(dto,Question.class);
        question.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        if(question.getQuestionType().equals(QuestionType.Subjective)){
            question.setDifficulty(DifficultyLevel.Subjective);
        }
        questionRepository.save(question);
        if(question.getQuestionType().equals(QuestionType.MCQ))
            generateQuestionOption(question.getId());
        return getQuestionDetailsInternal(question.getId());
    }

    private ReadQuestionInstructor getQuestionDetailsInternal(UUID questionId){
        ReadQuestionInstructor readQuestionInstructorDto;
        List<Object[]> result = questionRepository.findQuestionDetailsByIdAnsUserId(questionId,userId.getId());
        if(!result.isEmpty()){
            Object[] row = result.get(0);
            if(QuestionType.valueOf(row[2].toString()).equals(QuestionType.MCQ)){
                readQuestionInstructorDto = ReadQuestionInstructor.builder()
                        .questionId(idEncoder.encodeId((UUID) row[0],userId.getId()))
                        .examId(idEncoder.encodeId((UUID) row[1],userId.getId()))
                        .questionType(QuestionType.valueOf(row[2].toString()))
                        .questionText((String) row[3])
                        .questionImageUrl((String) row[4])
                        .correctAnswer(row[5] != null ? AnswerOption.valueOf(row[5].toString()) : null)
                        .suggestedAnswer(Arrays.asList((String[]) row[6]))
                        .difficulty(DifficultyLevel.valueOf(row[7].toString()))
                        .isAssigned((Boolean) row[8])
                        .createdAt(((Timestamp) row[9]).toLocalDateTime())
                        .updatedAt(((Timestamp) row[10]).toLocalDateTime())
                        .options(Arrays.asList((String[]) row[11]))
                        .optionsUrl(Arrays.asList((String[]) row[12]))
                        .build();
                FileMetadata optionPayload = FileMetadata.builder()
                        .questionId(readQuestionInstructorDto.getQuestionId())
                        .examId(readQuestionInstructorDto.getExamId())
                        .build();

                for(int i=0;i<4;i++){
                    if(readQuestionInstructorDto.getOptionsUrl().get(i)!=null){
                        optionPayload.setLocation(readQuestionInstructorDto.getOptionsUrl().get(i));
                        readQuestionInstructorDto.getOptionsUrl().set(i,ImageUrlGenerator.getUrl(tokenUtil.generateToken(optionPayload)));
                    }
                }

            }else{
                readQuestionInstructorDto = ReadQuestionInstructor.builder()
                        .questionId(idEncoder.encodeId((UUID) row[0],userId.getId()))
                        .examId(idEncoder.encodeId((UUID) row[1],userId.getId()))
                        .questionType(QuestionType.valueOf(row[2].toString()))
                        .questionText((String) row[3])
                        .questionImageUrl((String) row[4])
//                        .correctAnswer(row[5] != null ? AnswerOption.valueOf(row[5].toString()) : null)
                        .suggestedAnswer(Arrays.asList((String[]) row[6]))
                        .difficulty(DifficultyLevel.valueOf(row[7].toString()))
                        .isAssigned((Boolean) row[8])
                        .createdAt(((Timestamp) row[9]).toLocalDateTime())
                        .updatedAt(((Timestamp) row[10]).toLocalDateTime())
//                        .options(Arrays.asList((String[]) row[11]))
//                        .optionsUrl(Arrays.asList((String[]) row[12]))
                        .build();
            }

            FileMetadata payload = FileMetadata.builder()
                    .questionId(readQuestionInstructorDto.getQuestionId())
                    .examId(readQuestionInstructorDto.getExamId())
                    .build();
            if(readQuestionInstructorDto.getQuestionImageUrl()!=null && !readQuestionInstructorDto.getQuestionImageUrl().isEmpty()){
                payload.setLocation(readQuestionInstructorDto.getQuestionImageUrl());
                readQuestionInstructorDto.setQuestionImageUrl(ImageUrlGenerator.getUrl(tokenUtil.generateToken(payload)));
            }
        }else{
            throw new CustomException("Invalid id");
        }

        return readQuestionInstructorDto;
    }

    public ReadQuestionInstructor getQuestionDetailsInstructor(UUID questionId){
        return getQuestionDetailsInternal(idEncoder.decodeId(questionId,userId.getId()));
    }


    @Transactional
    public ReadQuestionInstructor updateQuestion(UpdateQuestionDto dto) {
        Question question = questionRepository.findByIdAndUserId(idEncoder.decodeId(dto.getQuestionId(),userId.getId()),userId.getId())
                .orElseThrow(()-> new CustomException("Invalid data"));
        dto.setExamId(idEncoder.decodeId(dto.getExamId(),userId.getId()));
        if(!question.getExamId().equals(dto.getExamId())){
            throw new CustomException("Invalid data");
        }
        if(examRepository.isExamReady(question.getExamId())){
            if(!question.getQuestionType().equals(dto.getQuestionType()) || !question.getDifficulty().equals(dto.getDifficulty())){
                throw new CustomException("Exam is ready, can't update question type or difficulty");
            }
        }
        // posibilities
        // MCQ MCQ
        // MCQ Subjective
        // Subjective Subjective
        // Subjective MCQ
        if(!question.getQuestionType().equals(dto.getQuestionType())){
            if(question.getQuestionType().equals(QuestionType.Subjective)){
                generateQuestionOption(question.getId());
            }else{
                deleteQuestionOption(question.getId(),question.getExamId());
            }
        }
        if(dto.getQuestionType().equals(QuestionType.MCQ)){
            updateQuestionOption(dto.getOptions(),question.getId());
        }
        mapper.map(dto,question);
        questionRepository.save(question);
        return getQuestionDetailsInternal(question.getId());
    }

    private void updateQuestionOption(List<String> options, UUID questionId) {
        for(int i=0;i<4;i++){
            QuestionOption questionOption = questionOptionRepository.findByQuestionIdAndOptionLabel(questionId,AnswerOption.values()[i]);
            questionOption.setOption(options.get(i));
            questionOptionRepository.save(questionOption);
        }
    }

    public Integer countQuestionByLevelAndExamId(UUID examId, DifficultyLevel level){
        return questionRepository.countQuestionByExamIdAndDifficulty(examId,level.name());
    }
}
