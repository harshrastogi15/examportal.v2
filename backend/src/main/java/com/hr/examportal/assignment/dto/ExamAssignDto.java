package com.hr.examportal.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamAssignDto {

    @NotNull(message = "exam id cant be null")
    private UUID examId;

    @NotNull(message = "user id cant be null")
    private UUID studentId;

}
