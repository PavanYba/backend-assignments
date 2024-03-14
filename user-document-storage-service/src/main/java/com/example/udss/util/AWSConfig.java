package com.example.udss.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AWSConfig {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

}
