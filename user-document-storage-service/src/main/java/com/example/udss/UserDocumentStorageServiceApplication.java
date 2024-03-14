package com.example.udss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class UserDocumentStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDocumentStorageServiceApplication.class, args);
    }

}
