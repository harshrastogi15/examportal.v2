package com.hr.examportal.image.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final MinioService minioService;
    public void uploadImage(MultipartFile file, String metadata) {
        System.out.println(metadata);
        minioService.uploadFile(file,"/testing");
        System.out.println(minioService.getPresignedUrl("testing/testing.png"));
    }

}


