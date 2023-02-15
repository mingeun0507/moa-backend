package com.hanamja.moa.api.controller;

import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final AmazonS3Uploader amazonS3Uploader;

    @PostMapping("/s3/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("images") MultipartFile multipartFile){
        try {
            String url = amazonS3Uploader.saveFileAndGetUrl(multipartFile);
            return ResponseEntity.ok().body(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}