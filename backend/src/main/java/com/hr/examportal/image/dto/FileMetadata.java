package com.hr.examportal.image.dto;
import lombok.Data;

@Data
public class FileMetadata {
    private String folderPath;
    private String description;
    private String createdBy;
}