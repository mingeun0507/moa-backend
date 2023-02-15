package com.hanamja.moa.api.controller;

import com.hanamja.moa.utils.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3UploadService s3UploadService;

    @PostMapping("/s3/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("images") MultipartFile multipartFile){
        try {
            String url = s3UploadService.saveFileAndGetUrl(multipartFile);
            return ResponseEntity.ok().body(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}