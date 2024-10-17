package com.market.core.s3.service;

import com.market.core.code.error.S3ErrorCode;
import com.market.core.exception.S3Exception;
import com.market.core.s3.dto.response.ImageUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUrlService {

    private final S3ImageService s3ImageService;

    /**
     * S3에 사진을 업로드합니다.
     */
    public ImageUrlResponse uploadImage(MultipartFile uploadImage) {
        // 파일 확장자 검사
        if (!isImageFile(uploadImage)) {
            throw new S3Exception(S3ErrorCode.INVALID_IMAGE_EXTENSION);
        }

        return ImageUrlResponse.builder()
                .imageUrl(s3ImageService.uploadImage(uploadImage))
                .build();
    }

    /**
     * S3에서 사진을 삭제합니다.
     */
    public void deleteImage(String imageUrl) {
        s3ImageService.deleteImage(imageUrl);
    }

    /**
     * 파일 확장자가 이미지 파일인지 확인합니다.
     */
    private boolean isImageFile(MultipartFile uploadImage) {
        String filename = uploadImage.getOriginalFilename();
        String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png");
    }
}
