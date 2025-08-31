package com.hr.examportal.image.service;


import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.question.entity.Question;
import com.hr.examportal.question.repository.QuestionRepository;
import com.hr.examportal.utils.IdEncoder;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.UserId;
import com.hr.examportal.utils.enums.LocationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final MinioService minioService;
    private final QuestionRepository questionRepository;
    private final UserId userId;
    private final IdEncoder idEncoder;
    private final TokenUtil tokenUtil;
    public Map<String,Object> uploadImage(MultipartFile file, FileMetadata metadata) {
        Question question = questionRepository.findByIdAndUserId(idEncoder.decodeId(metadata.getQuestionId(),userId.getId()),userId.getId())
                .orElseThrow(()-> new CustomException("Unable to access question"));

        String filePath = metadata.getLocationType() + "/img" + (file.getContentType().equals("image/jpeg")?".jpeg":".png");
        String objectName =  question.getExamId() + "/" + question.getId() +  "/"+ filePath;
        minioService.uploadFile(file,objectName);
        if(metadata.getLocationType().equals(LocationType.Question)){
            question.setQuestionImageUrl(filePath);
            questionRepository.save(question);
        }

        Map<String, Object> data = new HashMap<>();

        metadata.setLocation(filePath);
        metadata.setExamId(idEncoder.encodeId(question.getExamId(),userId.getId()));
        data.put("imageUrl",tokenUtil.generateToken(metadata));
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
}


