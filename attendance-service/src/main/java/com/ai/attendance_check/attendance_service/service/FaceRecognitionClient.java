package com.ai.attendance_check.attendance_service.service;

import com.ai.attendance_check.attendance_service.dto.FaceRecognitionResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class FaceRecognitionClient {

    private final WebClient faceRecognitionWebClient;

    public FaceRecognitionClient(@Qualifier("faceRecognitionWebClient") WebClient faceRecognitionWebClient) {
        this.faceRecognitionWebClient = faceRecognitionWebClient;
    }

    public FaceRecognitionResponse recognizeFace(MultipartFile file) {
        try {
            return faceRecognitionWebClient.post()
                    .uri("/api/face/recognize")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file",
                            new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                                @Override
                                public String getFilename() {
                                    return file.getOriginalFilename();
                                }
                            }))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Face API error: " + body)))
                    .bodyToMono(FaceRecognitionResponse.class) // map to DTO
                    .block(); // ❗ block since your service is not reactive
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}
