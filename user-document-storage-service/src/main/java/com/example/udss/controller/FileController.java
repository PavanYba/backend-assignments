package com.example.udss.controller;

import com.example.udss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchFile(
            @RequestParam String userName,
            @RequestParam String searchTerm
    ) {
        String result = fileService.searchFiles(userName, searchTerm);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(
            @RequestParam String userName,
            @RequestParam String fileName
    ) {
        return fileService.downloadFile(userName, fileName);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam String userName,
            @RequestParam("file") MultipartFile file
    ) {
        String result = fileService.uploadFile(userName, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
