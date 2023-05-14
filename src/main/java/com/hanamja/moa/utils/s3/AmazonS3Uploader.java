package com.hanamja.moa.utils.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanamja.moa.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Uploader {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${image.path}")
    private String imagePath;

    public String saveFileAndGetUrl(MultipartFile multipartFile) throws Exception {
        String s3FileName = imagePath + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getSize());

        amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public void deleteFile(String imageUrl) {
        int lastIndex = imageUrl.lastIndexOf('/') + 1;
        String fileName = imageUrl.substring(lastIndex);
        try {
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            String keyName = imagePath + decodedFileName;
            boolean isObjectExist = amazonS3.doesObjectExist(bucket, keyName);
            if (isObjectExist) {
                amazonS3.deleteObject(bucket, keyName);
            }
        } catch (Exception e) {
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미지 삭제에 실패했습니다.").build();
        }
    }
}
