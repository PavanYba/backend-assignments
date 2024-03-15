package com.example.pdapi.util;

import com.example.pdapi.util.GoogleMapsApiKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {"google.maps.api.key=test-api-key"})
public class GoogleMapsApiKeyTest {

    @Autowired
    private GoogleMapsApiKey apiKeyUtil;

    @Test
    public void testGetApiKey() {
        String apiKey = apiKeyUtil.getApiKey();
        assertEquals("test-api-key", apiKey);
    }
}
