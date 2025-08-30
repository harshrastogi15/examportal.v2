package com.hr.examportal.image.controller;

import com.hr.examportal.image.dto.FileMetadata;
import com.hr.examportal.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class imageController {
    private final ImageService imageService;
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> uploadImage(@RequestPart("file") MultipartFile file, @RequestPart("metadata") String metadata){

        imageService.uploadImage(file, metadata);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteImage(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

//    @GetMapping("/{token}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String token) throws Exception {
//        String objectName;
//        try {
//            objectName = TokenUtil.validateTokenAndGetObjectName(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        // Optional: check user access here
//
//        InputStream is = minioClient.getObject(
//                GetObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .build()
//        );
//
//        byte[] data = is.readAllBytes();
//        is.close();
//
//        // Detect type dynamically or use default
//        String contentType = objectName.endsWith(".png") ? "image/png" :
//                objectName.endsWith(".jpg") || objectName.endsWith(".jpeg") ? "image/jpeg" :
//                        "application/octet-stream";
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(data);
//    }

}
