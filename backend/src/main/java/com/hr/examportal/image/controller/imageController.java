package com.hr.examportal.image.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.examportal.exception.CustomException;
import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.image.service.ImageService;
import com.hr.examportal.utils.TokenUtil;
import com.hr.examportal.utils.enums.LocationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class imageController {
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final TokenUtil tokenUtil;
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String,Object>> uploadImage(@RequestPart("file") MultipartFile file, @RequestPart("metadata") String metadata) {
        FileMetadata fileMetadata;
        try {
            fileMetadata = objectMapper.readValue(metadata, FileMetadata.class);
        } catch (JsonProcessingException e) {
            throw new CustomException("metadata is not correct");
        }

        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new CustomException("Only JPEG and PNG images are allowed!");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !(originalFilename.toLowerCase().endsWith(".jpg") ||
                        originalFilename.toLowerCase().endsWith(".jpeg") ||
                        originalFilename.toLowerCase().endsWith(".png"))) {
            throw new CustomException("Invalid file extension. Only JPG and PNG allowed.");
        }


        return ResponseEntity.ok(imageService.uploadImage(file, fileMetadata));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Map<String,Object>> deleteImage(@Valid @RequestBody FileMetadata metadata){
        return ResponseEntity.ok(imageService.deleteImage(metadata));
    }

    @GetMapping("/{token}")
    public ResponseEntity<byte[]> getImage(@PathVariable String token){
        Map<String,Object> response = imageService.getImage(token);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.get("contentType").toString()))
                .body((byte[]) response.get("data"));
    }

}
