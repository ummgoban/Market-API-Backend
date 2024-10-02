package com.market.core.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.market.core.code.error.S3ErrorCode;
import com.market.core.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * S3에 이미지를 업로드 및 삭제하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class S3ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public final AmazonS3 amazonS3;

    /**
     * S3에 이미지 업로드
     */
    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * S3에서 이미지 삭제
     */
    public void deleteImage(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(bucketName, fileName);
    }
}