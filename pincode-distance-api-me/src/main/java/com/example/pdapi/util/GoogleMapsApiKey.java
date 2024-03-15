package com.example.pdapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleMapsApiKey {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
