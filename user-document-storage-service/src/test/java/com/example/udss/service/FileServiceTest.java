package com.example.udss.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileServiceTest {
@Mock
    private AmazonS3 amazonS3;
    private final String bucketName = "testBucket";
    private FileService fileService;

    @BeforeEach
    public void setup() {
        amazonS3 = mock(AmazonS3.class);
        fileService = new FileService(amazonS3, bucketName);

    }
    @Test
    public void testSearchFiles() {
        String userName = "user123";
        String searchTerm = "example";

        List<S3ObjectSummary> objectSummaries = Arrays.asList(
                createS3ObjectSummary("user123/file1_example.txt"),
                createS3ObjectSummary("user123/file2.txt"),
                createS3ObjectSummary("user123/file3_example.jpg")
        );

        List<String> expectedFiles = Arrays.asList(
                "user123/file1_example.txt",
                "user123/file3_example.jpg"
        );

        // Mock behavior of the S3 client
        ObjectListing objectListing = new ObjectListing();
        objectListing.setBucketName(bucketName);
        objectListing.getObjectSummaries().addAll(objectSummaries);
        when(amazonS3.listObjects(bucketName, "user123/")).thenReturn(objectListing);

        // Call the method to test
        String result = fileService.searchFiles(userName, searchTerm);

        // Verify the result
        assertEquals(String.join("\n", expectedFiles), result);
    }

    private S3ObjectSummary createS3ObjectSummary(String key) {
        S3ObjectSummary summary = new S3ObjectSummary();
        summary.setKey(key);
        return summary;
    }


    @Test
    public void testDownloadFile() {
        String userName = "testUser";
        String fileName = "testFile.txt";
        String key = userName + "/" + fileName;
        byte[] fileContent = "File Content".getBytes();

        S3Object object = mock(S3Object.class);
        S3ObjectInputStream inputStream = new S3ObjectInputStream(new ByteArrayInputStream(fileContent), null);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/plain");

        when(amazonS3.getObject(bucketName, key)).thenReturn(object);
        when(object.getObjectContent()).thenReturn(inputStream);
        when(object.getObjectMetadata()).thenReturn(objectMetadata);

        ResponseEntity<?> response = fileService.downloadFile(userName, fileName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileContent.length, ((byte[]) Objects.requireNonNull(response.getBody())).length);
        assertEquals("attachment; filename=" + fileName, response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("text/plain", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void testUploadFile() throws IOException {
        String userName = "testUser";
        String fileName = "testFile.txt";
        String key = userName + "/" + fileName;
        byte[] fileContent = "File Content".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/plain", fileContent);

        when(amazonS3.putObject(bucketName, key, multipartFile.getInputStream(), null)).thenReturn(null);

        String result = fileService.uploadFile(userName, multipartFile);

        assertEquals("File uploaded successfully: " + fileName, result);
    }

}
