package com.hanamja.moa.utils.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void store(String fullPath, MultipartFile multipartFile) {
        long size = multipartFile.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, multipartFile.getInputStream(), objectMetaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }
}
