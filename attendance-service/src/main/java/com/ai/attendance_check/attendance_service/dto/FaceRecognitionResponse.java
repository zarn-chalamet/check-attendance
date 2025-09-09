package com.ai.attendance_check.attendance_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaceRecognitionResponse {
    private String user_id;
    private Double confidence;
}
