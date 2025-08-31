package com.hr.examportal.image.dto;
import com.hr.examportal.utils.enums.LocationType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class FileMetadata {
    @NotNull
    private LocationType locationType;
    @NotNull
    private UUID questionId;
    private UUID examId;
    private String location;
}