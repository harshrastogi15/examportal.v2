package com.hr.examportal.image.service;


import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.question.entity.Question;
import com.hr.examportal.question.entity.QuestionOption;
import com.hr.examportal.question.repository.QuestionOptionRepository;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.ImageUrlGenerator;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.AnswerOption;
import com.hr.examportal.utils.enums.LocationType;
import com.hr.examportal.utils.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final MinioService minioService;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserId userId;
    private final IdEncoder idEncoder;
    private final TokenUtil tokenUtil;
    public Map<String,Object> uploadImage(MultipartFile file, FileMetadata metadata) {
        Question question = questionRepository.findByIdAndUserId(idEncoder.decodeId(metadata.getQuestionId(),userId.getId()),userId.getId())
                .orElseThrow(()-> new CustomException("Unable to access question"));

        String filePath = metadata.getLocationType() + "/img" + (file.getContentType().equals("image/jpeg")?".jpeg":".png");
        String objectName =  question.getExamId() + "/" + question.getId() +  "/"+ filePath;
        if(metadata.getLocationType().equals(LocationType.Question)){
            minioService.uploadFile(file,objectName);
            question.setQuestionImageUrl(filePath);
            questionRepository.save(question);
        }else if(question.getQuestionType().equals(QuestionType.MCQ)){
            minioService.uploadFile(file,objectName);
            QuestionOption option = questionOptionRepository.findByQuestionIdAndOptionLabel(question.getId(), AnswerOption.valueOf(metadata.getLocationType().toString().substring(metadata.getLocationType().toString().length() - 1)));
            option.setImageUrl(filePath);
            questionOptionRepository.save(option);
        }else{
            throw new CustomException("Invalid data");
        }

        Map<String, Object> data = new HashMap<>();

        metadata.setLocation(filePath);
        metadata.setExamId(idEncoder.encodeId(question.getExamId(),userId.getId()));
        data.put("imageUrl", ImageUrlGenerator.getUrl(tokenUtil.generateToken(metadata)));
        return data;
    }

    public Map<String, Object> getImage(String token) {
        try {
        Map<String,Object> res = new HashMap<>();
        FileMetadata fileMetadata = tokenUtil.validateTokenAndGetObjectName(token,FileMetadata.class);
        String objectName = idEncoder.encodeId(fileMetadata.getExamId(),userId.getId())
                + "/"
                + idEncoder.encodeId(fileMetadata.getQuestionId(),userId.getId())
                + "/"
                + fileMetadata.getLocation();
        System.out.println(objectName);
        InputStream is = minioService.getObject(objectName);
        byte[] data = is.readAllBytes();
        is.close();
        String contentType = fileMetadata.getLocation().endsWith(".png") ? "image/png" :
                    fileMetadata.getLocation().endsWith(".jpg") || fileMetadata.getLocation().endsWith(".jpeg") ? "image/jpeg" :
                            "application/octet-stream";

        res.put("contentType",contentType);
        res.put("data",data);
        return res;
        } catch (Exception e) {
            throw new CustomException("Unable to serve");
        }
    }

    public Map<String,Object> deleteImage(FileMetadata metadata) {
        Question question = questionRepository.findByIdAndUserId(idEncoder.decodeId(metadata.getQuestionId(),userId.getId()),userId.getId())
                .orElseThrow(()-> new CustomException("Unable to access question"));

        if(metadata.getLocationType().equals(LocationType.Question)){
            String filePath = question.getQuestionImageUrl();
            if(filePath==null){
                throw new CustomException("Not exist");
            }
            String objectName =  question.getExamId() + "/" + question.getId() +  "/"+ filePath;
            minioService.deleteFile(objectName);
            question.setQuestionImageUrl(null);
            questionRepository.save(question);
        }else if(question.getQuestionType().equals(QuestionType.MCQ)){
            QuestionOption option = questionOptionRepository.findByQuestionIdAndOptionLabel(question.getId(), AnswerOption.valueOf(metadata.getLocationType().toString().substring(metadata.getLocationType().toString().length() - 1)));
            String filePath = option.getImageUrl();
            if(filePath==null){
                throw new CustomException("Not exist");
            }
            String objectName =  question.getExamId() + "/" + question.getId() +  "/"+ filePath;
            minioService.deleteFile(objectName);
            option.setImageUrl(null);
            questionOptionRepository.save(option);
        }else{
            throw new CustomException("Invalid data");
        }
        return new HashMap<>(){{put("Status","Success");}};
    }

    public void deleteFile(UUID examId, UUID questionId, String imageUrl) {
        String objectName =  examId + "/" + questionId +  "/"+ imageUrl;
        minioService.deleteFile(objectName);
    }
}


