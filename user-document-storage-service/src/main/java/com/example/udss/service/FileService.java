package com.example.udss.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class FileService {

    private final AmazonS3 s3Client;
    private final String bucketName;

    @Autowired
    public FileService(AmazonS3 s3Client, @Value("${aws.s3.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String searchFiles(String userName, String searchTerm) {
        String userFolder = userName + "/";

        List<String> fileList = s3Client.listObjects(bucketName, userFolder)
                .getObjectSummaries()
                .stream()
                .filter(s -> s.getKey().contains(searchTerm))
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        if (fileList.isEmpty()) {
            return "No files found with the search term: " + searchTerm;
        } else {
            return String.join("\n", fileList);
        }

    }

    public ResponseEntity<?> downloadFile(String userName, String fileName) {
        String key = userName + "/" + fileName;
        try {
            S3Object object = s3Client.getObject(bucketName, key);
            InputStream inputStream = object.getObjectContent();

            byte[] content = inputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, object.getObjectMetadata().getContentType());
            headers.setContentLength(content.length);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
        }
    }

    public String uploadFile(String userName, MultipartFile file) {
        try {
            String key = userName + "/" + Objects.requireNonNull(file.getOriginalFilename());

            s3Client.putObject(bucketName, key, file.getInputStream(), null);

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file: " + file.getOriginalFilename();
        }
    }
}
