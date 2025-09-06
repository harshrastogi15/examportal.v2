package com.hr.examportal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.examportal.image.dto.FileMetadata;
import lombok.RequiredArgsConstructor;

public class ImageUrlGenerator {

    public static String getUrl(String token) {
        String imageUrl = "http://localhost:8080/api/image/";
        return imageUrl + token;
    }
}
