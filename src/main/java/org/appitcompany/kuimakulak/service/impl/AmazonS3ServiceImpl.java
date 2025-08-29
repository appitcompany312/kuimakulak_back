package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.service.AmazonS3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or missing");
        }

        try {
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();

        } catch (IOException e) {
            throw new RuntimeException("File processing failed", e);
        } catch (SdkException e) {
            throw new RuntimeException("S3 upload failed", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file upload", e);
        }
    }

    public void deleteFile(String url) {
        String key = extractFileKeyFromUrl(url);
        if (key.isBlank()) {
            throw new IllegalArgumentException("File key is missing or empty");
        }
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (SdkException e) {
            throw new RuntimeException("S3 delete failed", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file deletion", e);
        }
    }

    private String extractFileKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            // Путь обычно начинается с косой черты, поэтому мы удаляем ее.
            if (path.startsWith("/")) {
                return path.substring(1);
            }
            return path;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format.", e);
        }
    }
}
