package com.hanamja.moa.service;

import com.hanamja.moa.utils.s3.AmazonS3ResourceStorage;
import com.hanamja.moa.utils.s3.FileDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
    @Value("${RESOURCE_S3}")
    private String RESOURCE_S3;
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public String saveFileAndGetUrl(MultipartFile multipartFile) {
        FileDetail fileDetail = FileDetail.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return RESOURCE_S3 + fileDetail.getPath();
    }

}
