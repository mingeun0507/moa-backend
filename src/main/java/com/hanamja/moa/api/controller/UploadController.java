package com.hanamja.moa.api.controller;

import com.hanamja.moa.api.dto.upload.response.UploadResponseDto;
import com.hanamja.moa.exception.custom.UserInputException;
import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private final AmazonS3Uploader amazonS3Uploader;

    @Operation(summary = "이미지 업로드")
    @PostMapping("/image")
    public ResponseEntity<UploadResponseDto> uploadFile(@RequestPart("image") MultipartFile multipartFile){
        try {
            String url = amazonS3Uploader.saveFileAndGetUrl(multipartFile);
            return ResponseEntity.ok(new UploadResponseDto(url));
        } catch (Exception e) {
            throw UserInputException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("이미지 업로드에 실패했습니다.")
                    .build();
        }
    }
}